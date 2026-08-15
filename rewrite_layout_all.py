import re, glob

MASTER = "app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate02_PixelPerfect.kt"
master_src = open(MASTER).read()

files = sorted(glob.glob("app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate*_PixelPerfect.kt"))
files = [f for f in files if "Template02_" not in f]

done = []
for path in files:
    old = open(path).read()
    num = re.search(r'ResumeTemplate(\d+)_PixelPerfect', old).group(1)
    accent_m = re.search(r'val accent = Color\(0xFF[0-9A-Fa-f]+\)', old)
    accent_line = accent_m.group(0) if accent_m else None

    new_src = master_src.replace("ResumeTemplate02_PixelPerfect", f"ResumeTemplate{num}_PixelPerfect")
    if accent_line:
        new_src = re.sub(r'val accent = Color\(0xFF[0-9A-Fa-f]+\)', accent_line, new_src)

    open(path, "w").write(new_src)
    done.append(f"Template{num}")

print("Rewrote:", ", ".join(done))
