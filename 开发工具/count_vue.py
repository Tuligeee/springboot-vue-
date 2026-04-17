import os
import glob

base_dir = r"C:\Users\Tuilg\OneDrive\Desktop\springboot+vue gaokao\前端代码\example-ui\src\views"
vue_files = glob.glob(os.path.join(base_dir, '**/*.vue'), recursive=True)
print(f"Total vue files: {len(vue_files)}")
