import re

MASTER = "app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate02_PixelPerfect.kt"
master_src = open(MASTER).read()

TARGET = "app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate05_PixelPerfect.kt"
old = open(TARGET).read()

num = "05"
accent_m = re.search(r'val accent = Color\(0xFF[0-9A-Fa-f]+\)', old)
accent_line = accent_m.group(0) if accent_m else None

new_src = master_src.replace("ResumeTemplate02_PixelPerfect", f"ResumeTemplate{num}_PixelPerfect")
if accent_line:
    new_src = re.sub(r'val accent = Color\(0xFF[0-9A-Fa-f]+\)', accent_line, new_src)

open(TARGET, "w").write(new_src)
print(f"Rewrote Template{num}")
