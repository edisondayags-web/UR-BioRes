letter_type_path = "app/src/main/java/com/saltech/urdocs/model/LetterType.kt"
repo_path = "app/src/main/java/com/saltech/urdocs/data/GeminiRepository.kt"

# ---------- 1. Add JOBSEEKER_OATH to the enum ----------
with open(letter_type_path) as f:
    src1 = f.read()

anchor1 = '    CUSTOM("Custom / Iba pa")'
if anchor1 not in src1:
    print("ABORT: enum anchor not found in LetterType.kt")
    raise SystemExit(1)

new1 = '    JOBSEEKER_OATH("Oath of Undertaking (First-Time Jobseeker)"),\n' + anchor1
src1 = src1.replace(anchor1, new1, 1)

with open(letter_type_path, "w") as f:
    f.write(src1)
print("PATCHED:", letter_type_path)

# ---------- 2. Add configs to GeminiRepository ----------
with open(repo_path) as f:
    src2 = f.read()

anchor2 = "        LetterType.CUSTOM to LetterConfig("
if anchor2 not in src2:
    print("ABORT: config anchor not found in GeminiRepository.kt")
    raise SystemExit(1)

new_configs = '''        LetterType.JOBSEEKER_OATH to LetterConfig(
            "Oath of Undertaking (First-Time Jobseeker)",
            "buong pangalan, address, edad, kailan una mag-a-apply ng trabaho -- ito ay pormal na sinumpaang salaysay (oath) na sumusunod sa RA 11261 (First-Time Jobseekers Assistance Act), nagsasaad na ito ang UNANG BESES ng requester na mag-aaplay ng trabaho at hindi pa sila nakinabang sa benepisyong ito dati; gamitin ang tamang legal na tono ng isang sinumpaang salaysay"
        ),
        LetterType.MEDICAL_AUTHORIZATION to LetterConfig(
            "Medical Authorization Letter",
            "buong pangalan ng pasyente, relasyon ng nagbibigay ng awtorisasyon, buong pangalan ng taong pinagkakatiwalaan/awtorisado, dahilan (hal. medical procedure, paggamot, o pagsama sa ospital), petsa o saklaw ng awtorisasyon"
        ),
        LetterType.AFFIDAVIT_LOSS to LetterConfig(
            "Affidavit of Loss",
            "buong pangalan ng nagsasalaysay, address, deskripsyon ng nawalang item/dokumento, kung kailan at saan ito nawala, layunin ng affidavit (hal. para sa palit ng ID, requirement sa insurance) -- gamitin ang tamang legal na tono ng isang sinumpaang salaysay"
        ),
        LetterType.MEDICAL_ASSISTANCE to LetterConfig(
            "Medical/Financial Assistance Request",
            "buong pangalan ng humihingi, address, dahilan ng kahilingan (uri ng sakit o pangangailangang medikal), tinatayang gastos kung meron, kanino ipapadala (hal. DSWD, ospital, LGU, ahensya)"
        ),
        LetterType.PARENTAL_CONSENT to LetterConfig(
            "Parental/Guardian Consent Letter",
            "buong pangalan ng magulang/guardian, buong pangalan ng anak/menor de edad, dahilan ng consent (hal. field trip, paglalakbay, medical procedure, trabaho), petsa o saklaw ng pahintulot"
        ),
''' + anchor2

src2 = src2.replace(anchor2, new_configs, 1)

with open(repo_path, "w") as f:
    f.write(src2)
print("PATCHED:", repo_path)
