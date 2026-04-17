import pymysql

conn = pymysql.connect(host='localhost', port=3306, user='root', password='123456', database='ry')
cursor = conn.cursor()

cursor.execute("SELECT * FROM sys_menu WHERE menu_id IN (2061, 2063, 2064, 2080)")
rows = cursor.fetchall()
print("Menus:")
for row in rows:
    print(row)

cursor.execute("SELECT * FROM sys_role_menu WHERE menu_id IN (2061, 2063, 2064, 2080)")
rows = cursor.fetchall()
print("Role-Menus:")
for row in rows:
    print(row)

cursor.execute("SELECT m.menu_id, m.menu_name, m.perms FROM sys_menu m JOIN sys_role_menu rm ON m.menu_id = rm.menu_id WHERE rm.role_id IN (2, 100)")
rows = cursor.fetchall()
print("Perms for roles 2 and 100:")
for row in rows:
    # check if 'aspiration' in perms
    if row[2] and 'aspiration' in row[2]:
        print(row)

conn.close()
