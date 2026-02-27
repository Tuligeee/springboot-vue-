package com.mock.example.modules.entrance.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationBody;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationFormBody;
import com.mock.example.interfaces.body.entrance.aspiration.EvaluateBody;
import com.mock.example.interfaces.vo.entrance.aspiration.AspirationFormVo;
import com.mock.example.interfaces.vo.entrance.aspiration.AspirationSelectVo;
import com.mock.example.interfaces.vo.entrance.aspiration.EvaluateResultVo;
import com.mock.example.modules.entrance.entity.enums.TagEnum;
import com.mock.example.modules.entrance.entity.model.*;
import com.mock.example.modules.entrance.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 志愿管理业务层
 *
 * @author: Mock
 * @date: 2023-04-05 09:08:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CeAspirationService {

    private final ICeCollegeRepo collegeRepo;

    private final ICeProfessionRepo professionRepo;

    private final ICeStudentRepo studentRepo;

    private final ICeTagRelRepo tagRelRepo;

    private final ICeScoreLineRepo scoreLineRepo;

    private final ICeAspirationRepo aspirationRepo;

    private final ICeAspirationDetailRepo aspirationDetailRepo;

    /**
     * 测评分析
     *
     * @param evaluateBody 评估请求体
     * @return 评测结果
     */
    public EvaluateResultVo evaluate(EvaluateBody evaluateBody) {
        //评估出来的专业
        List<String> allProfessionNos = Lists.newArrayList();
        //查询学生
        CeStudent student = studentRepo.selectStudentByNo(evaluateBody.getStudentNo());
        if (student == null) {
            throw new BizException("未搜到该学号的学生");
        }

        Integer achievement = student.getAchievement();
        //查询最近一年满足的专业分数线
        Integer year = scoreLineRepo.getLastYear();
        List<CeScoreLine> satisfyScoreLines = scoreLineRepo.selectScoreLineLeScore(year, achievement);

        //通过分数评估
        if (BooleanUtils.isTrue(evaluateBody.getByScore())) {
            evaluateByScore(allProfessionNos, satisfyScoreLines);
        }

        //通过标签评估
        if (BooleanUtils.isTrue(evaluateBody.getByTag()) &&
                (CollUtil.isNotEmpty(allProfessionNos) || BooleanUtils.isFalse(evaluateBody.getByScore()))
        ) {
            evaluateByTag(allProfessionNos, student, evaluateBody.getByScore());
        }
        //组装评估结果
        List<String> result = generateResult(allProfessionNos, satisfyScoreLines);

        return new EvaluateResultVo().setResults(result);
    }

    /**
     * 填报志愿
     *
     * @param aspirationFormBody 志愿填报对象
     * @return 结果
     */
    @Transactional
    public Boolean addFrom(AspirationFormBody aspirationFormBody) {
        Long userId = SecurityUtil.getUserId();
        CeStudent student = studentRepo.selectStudentByUserId(userId);

        //删除志愿
        deleteAspiration(student);

        //更新志愿
        CeAspiration aspiration = new CeAspiration();
        aspiration.setStudentNo(student.getStudentNo());
        aspiration.setEntranceYear(DateUtil.year(new Date()));
        aspiration.setCreatedUser(SecurityUtil.getUsername());
        aspirationRepo.save(aspiration);
        saveAspirationDetail(student.getStudentNo(), aspirationFormBody.getProfessionNo1(),1);
        saveAspirationDetail(student.getStudentNo(), aspirationFormBody.getProfessionNo2(),2);
        saveAspirationDetail(student.getStudentNo(), aspirationFormBody.getProfessionNo3(),3);

        return Boolean.TRUE;
    }

    private void deleteAspiration(CeStudent student) {
        aspirationRepo.deleteByStudentNo(student.getStudentNo());
        aspirationDetailRepo.deleteByStudentNo(student.getStudentNo());
    }

    private void saveAspirationDetail(String studentNo, String professionNo, Integer sort) {
        CeAspirationDetail ceAspirationDetail = new CeAspirationDetail();
        CeProfession ceProfession = professionRepo.selectByProfessionNo(professionNo);
        CeCollege ceCollege = collegeRepo.selectCollegeByNo(ceProfession.getCollegeNo());

        ceAspirationDetail.setStudentNo(studentNo);
        ceAspirationDetail.setProfessionNo(professionNo);
        ceAspirationDetail.setProfessionName(ceProfession.getProfessionName());
        ceAspirationDetail.setCollegeNo(ceCollege.getCollegeNo());
        ceAspirationDetail.setCollegeName(ceCollege.getCollegeName());
        ceAspirationDetail.setCreatedUser(SecurityUtil.getUsername());
        ceAspirationDetail.setAspirationBatch(1);
        ceAspirationDetail.setProfessionSort(sort);

        aspirationDetailRepo.save(ceAspirationDetail);
    }

    /**
     * 查询志愿列表
     *
     * @param aspirationBody 志愿请求对象
     * @return 志愿列表
     */
    public List<CeAspiration> selectAspirationList(AspirationBody aspirationBody) {
        CeAspiration aspiration = EntityCopyUtil.copyEntity(CeAspiration.class, aspirationBody);
        List<CeAspiration> aspirations = aspirationRepo.selectAspirationList(aspiration);

        //组装学生名称
        List<String> studentNos = aspirations.stream().map(
                CeAspiration::getStudentNo
        ).collect(Collectors.toList());
        Map<String, String> studentMap = studentRepo.selectByStudentNos(studentNos)
                .stream().collect(Collectors.toMap(
                        CeStudent::getStudentNo, CeStudent::getStudentName,
                        (v1, v2) -> v1
                ));
        aspirations.forEach(po -> po.setStudentName(
                studentMap.getOrDefault(po.getStudentNo(), StrUtil.EMPTY)
        ));

        return aspirations;
    }

    /**
     * 填报详情
     *
     * @param studentNo 学生编号
     * @return 填报详情
     */
    public String aspirationDetail(String studentNo){
        List<CeAspirationDetail> aspirationDetails = aspirationDetailRepo.selectAspirationDetailList(studentNo);
        String detail = StrUtil.EMPTY;
        for(CeAspirationDetail aspirationDetail : aspirationDetails){
            detail =  detail + "第" + aspirationDetail.getProfessionSort() + "志愿："
                    + aspirationDetail.getCollegeName() + " -> " + aspirationDetail.getProfessionName()
                    + ";  ";
        }

        return detail;
    }

    /**
     * 志愿填报表单
     *
     * @return 志愿填报表单
     */
    public AspirationFormVo selectItem() {
        //查出所有院校
        List<CeCollege> ceColleges = collegeRepo.list();
        //查出所有专业
        List<CeProfession> ceProfessions = professionRepo.list();
        Map<String, List<CeProfession>> ceProfessionsMap = ceProfessions.stream()
                .collect(Collectors.groupingBy(CeProfession::getCollegeNo));
        //组装
        List<AspirationSelectVo> aspirationSelectVos = ceColleges.stream().map(
                ceCollege -> {
                    AspirationSelectVo aspirationSelectVo = new AspirationSelectVo();
                    aspirationSelectVo.setLabel(ceCollege.getCollegeName());
                    aspirationSelectVo.setValue(ceCollege.getCollegeNo());
                    aspirationSelectVo.setChildren(
                            ceProfessionsMap.getOrDefault(ceCollege.getCollegeNo(), Lists.newArrayList())
                                    .stream().map(
                                            ceProfession -> {
                                                AspirationSelectVo selectVo = new AspirationSelectVo();
                                                selectVo.setLabel(ceProfession.getProfessionName());
                                                selectVo.setValue(ceProfession.getProfessionNo());
                                                return selectVo;
                                            }
                                    ).collect(Collectors.toList())
                    );

                    return aspirationSelectVo;
                }
        ).collect(Collectors.toList());


        Long userId = SecurityUtil.getUserId();
        CeStudent student = studentRepo.selectStudentByUserId(userId);
        List<CeAspirationDetail> ceAspirationDetails =
                aspirationDetailRepo.selectAspirationDetailList(student.getStudentNo());

        AspirationFormVo aspirationFormVo = new AspirationFormVo();
        aspirationFormVo.setItems(aspirationSelectVos);
        for(int i=0; i< ceAspirationDetails.size(); i++){
            if(i == 0){
                aspirationFormVo.setProfessionNo1(ceAspirationDetails.get(i).getProfessionNo());
            }
            if(i == 1){
                aspirationFormVo.setProfessionNo2(ceAspirationDetails.get(i).getProfessionNo());
            }
            if(i == 2){
                aspirationFormVo.setProfessionNo3(ceAspirationDetails.get(i).getProfessionNo());
            }
        }

        return aspirationFormVo;
    }


    private void evaluateByScore(List<String> allProfessionNos, List<CeScoreLine> satisfyScoreLines) {
        List<String> professionNos =
                satisfyScoreLines.stream().map(CeScoreLine::getProfessionNo).collect(Collectors.toList());

        allProfessionNos.addAll(professionNos);
    }

    private void evaluateByTag(List<String> allProfessionNos, CeStudent student, Boolean byTag) {
        //查询学生标签
        List<CeTagRel> studentTagRelList = tagRelRepo.selectTagsByTypeAndRelId(TagEnum.STUDENT, student.getId());
        //通过标签筛选专业
        List<CeTagRel> professionTagRelList = tagRelRepo.selectTagsByNames(
                TagEnum.PROFESSION,
                studentTagRelList.stream().map(CeTagRel::getTagName).collect(Collectors.toList())
        );
        List<String> professionNos = professionRepo.selectProfessionByIds(
                professionTagRelList.stream().map(CeTagRel::getRelId).collect(Collectors.toList())
        ).stream().map(
                CeProfession::getProfessionNo
        ).collect(Collectors.toList());

        if (BooleanUtils.isTrue(byTag)) {
            //取交集
            allProfessionNos.retainAll(professionNos);
        } else {
            //返回标签结果
            allProfessionNos.addAll(professionNos);
        }

    }

    private List<String> generateResult(List<String> allProfessionNos, List<CeScoreLine> satisfyScoreLines) {
        List<CeProfession> ceProfessions = professionRepo.selectProfessionByNos(allProfessionNos);
        //专业map
        Map<String, List<CeProfession>> professionMap = ceProfessions.stream()
                .collect(Collectors.groupingBy(CeProfession::getCollegeNo));
        //院校map
        Map<String, CeCollege> collegeMap = collegeRepo.selectCollegeListByNos(
                ceProfessions.stream().map(CeProfession::getCollegeNo).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(
                CeCollege::getCollegeNo, Function.identity(), (v1, v2) -> v1
        ));
        //分数Map
        Map<String, Integer> scoreLineMap = satisfyScoreLines.stream().collect(Collectors.toMap(
                CeScoreLine::getProfessionNo, CeScoreLine::getScore, (v1, v2) -> v1
        ));

        //组装筛选结果
        List<String> result = Lists.newArrayList();
        professionMap.forEach((collegeNo, professions) -> {
            result.add(
                    //拼接搜索结果
                    collegeMap.getOrDefault(collegeNo, new CeCollege()).getCollegeName()
                            + ": "
                            + StrUtil.join("; ",
                            professions.stream().map(
                                    profession ->
                                            profession.getProfessionName() + "(" +
                                                    scoreLineMap.getOrDefault(profession.getProfessionNo(), 0) + "分"
                                                    + ")"
                            ).collect(Collectors.toList()))
            );
        });
        return result;
    }

}

  