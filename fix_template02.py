import re

path = "app/src/main/java/com/saltech/urdocs/ui/screens/ResumeTemplateFormScreen.kt"
with open(path, "r") as f:
    content = f.read()

start_marker = "fun ResumeTemplate02Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {"
start = content.index(start_marker)

i = start + len(start_marker)
depth = 1
while depth > 0:
    if content[i] == "{":
        depth += 1
    elif content[i] == "}":
        depth -= 1
    i += 1
end = i

new_func = '''fun ResumeTemplate02Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    com.saltech.urdocs.ui.templates.ResumeTemplate02_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = "",
        exp3Position = "", exp3Company = "", exp3Dates = "", exp3Desc = "",
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = "",
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    else -> data
                }
            )
        }
    )
}'''

content = content[:start] + new_func + content[end:]
with open(path, "w") as f:
    f.write(content)

print("Done! Template02 function replaced.")
