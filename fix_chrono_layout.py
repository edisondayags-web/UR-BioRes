#!/usr/bin/env python3
import sys

path = "app/src/main/java/com/saltech/urdocs/ui/screens/ChronologicalResumeScreen.kt"

old = '''                // ===== TWO COLUMNS =====
                Row(modifier = Modifier.weight(1f)) {
                    // LEFT COLUMN (wider) — experience, education
                    Column(modifier = Modifier.weight(2f)) {
                        SectionHeader2("", "PROFESSIONAL EXPERIENCE")
                        data.work.forEachIndexed { i, entry ->
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                                MiniField2("", entry.company, Modifier.weight(1f)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(company = v) })
                                }
                                MiniField2("", entry.from, Modifier.width(60.dp)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(from = v) })
                                }
                                Text(" - ", fontSize = 15.sp, color = Color.Black)
                                MiniField2("", entry.to, Modifier.width(60.dp)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(to = v) })
                                }
                            }
                            MiniField2("", entry.role, Modifier.fillMaxWidth()) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(role = v) })
                            }
                            BulletLines2(entry.bullets) { idx, v ->
                                val newBullets = entry.bullets.toMutableList().also { it[idx] = v }
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(bullets = newBullets) })
                            }
                            if (i != data.work.lastIndex) Spacer(Modifier.height(12.dp))
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "EDUCATION")
                        EduLine(data.eduSchool, bold = false) { data = data.copy(eduSchool = it) }
                        EduLine(data.eduDegree, bold = true) { data = data.copy(eduDegree = it) }
                        EduLine(data.eduYear, bold = false) { data = data.copy(eduYear = it) }
                    }

                    Spacer(Modifier.width(20.dp))

                    // RIGHT COLUMN (narrower) — soft skills, technical skills, languages, interests
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeader2("", "SOFT SKILLS")
                        BulletLines2(data.softSkills) { idx, v ->
                            data = data.copy(softSkills = data.softSkills.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "TECHNICAL SKILLS")
                        BulletLines2(data.technicalSkills) { idx, v ->
                            data = data.copy(technicalSkills = data.technicalSkills.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "LANGUAGES")
                        BulletLines2(data.languages) { idx, v ->
                            data = data.copy(languages = data.languages.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "INTERESTS")
                        BulletLines2(data.interests) { idx, v ->
                            data = data.copy(interests = data.interests.toMutableList().also { it[idx] = v })
                        }
                    }
                }'''

new = '''                // ===== SINGLE COLUMN (ATS-safe order: Skills -> Experience -> Education -> Languages -> Interests) =====
                Column(modifier = Modifier.weight(1f)) {

                    SectionHeader2("", "SKILLS")
                    BulletLines2(data.technicalSkills) { idx, v ->
                        data = data.copy(technicalSkills = data.technicalSkills.toMutableList().also { it[idx] = v })
                    }
                    BulletLines2(data.softSkills) { idx, v ->
                        data = data.copy(softSkills = data.softSkills.toMutableList().also { it[idx] = v })
                    }
                    Spacer(Modifier.height(14.dp))

                    SectionHeader2("", "PROFESSIONAL EXPERIENCE")
                    data.work.forEachIndexed { i, entry ->
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                            MiniField2("", entry.company, Modifier.weight(1f)) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(company = v) })
                            }
                            MiniField2("", entry.from, Modifier.width(60.dp)) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(from = v) })
                            }
                            Text(" - ", fontSize = 15.sp, color = Color.Black)
                            MiniField2("", entry.to, Modifier.width(60.dp)) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(to = v) })
                            }
                        }
                        MiniField2("", entry.role, Modifier.fillMaxWidth()) { v ->
                            data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(role = v) })
                        }
                        BulletLines2(entry.bullets) { idx, v ->
                            val newBullets = entry.bullets.toMutableList().also { it[idx] = v }
                            data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(bullets = newBullets) })
                        }
                        if (i != data.work.lastIndex) Spacer(Modifier.height(12.dp))
                    }
                    Spacer(Modifier.height(14.dp))

                    SectionHeader2("", "EDUCATION")
                    EduLine(data.eduSchool, bold = false) { data = data.copy(eduSchool = it) }
                    EduLine(data.eduDegree, bold = true) { data = data.copy(eduDegree = it) }
                    EduLine(data.eduYear, bold = false) { data = data.copy(eduYear = it) }
                    Spacer(Modifier.height(14.dp))

                    SectionHeader2("", "LANGUAGES")
                    BulletLines2(data.languages) { idx, v ->
                        data = data.copy(languages = data.languages.toMutableList().also { it[idx] = v })
                    }
                    Spacer(Modifier.height(14.dp))

                    SectionHeader2("", "INTERESTS")
                    BulletLines2(data.interests) { idx, v ->
                        data = data.copy(interests = data.interests.toMutableList().also { it[idx] = v })
                    }
                }'''

with open(path, "r", encoding="utf-8") as f:
    content = f.read()

count = content.count(old)
if count == 0:
    print("NO MATCH FOUND. Wala akong nahanap na exact match — baka nagbago na yung file. Hindi ko ginalaw yung file mo.")
    sys.exit(1)
elif count > 1:
    print(f"FOUND {count} MATCHES — dapat 1 lang. Hindi ko ginalaw yung file mo para safe.")
    sys.exit(1)

content = content.replace(old, new)
with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("Done! Na-update na yung ChronologicalResumeScreen.kt sa single-column ATS layout.")

