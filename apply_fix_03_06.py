import re

# ---------- TEMPLATE 03 ----------
f = "app/src/main/assets/templates/ai_template_more_03.html"
html = open(f, encoding="utf-8").read()

# 1. Add Career Objective before Education
old = '''    <div class="content">
      <div class="m-sec">
        <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Education</div></div>'''
new = '''    <div class="content">
      <div class="m-sec">
        <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Career Objective</div></div>
        <div class="m-body">
          <div class="e-t" contenteditable="true">To obtain an internship or entry-level position where I can apply my skills, learn from experienced professionals, and contribute to the growth of the organization.</div>
        </div>
      </div>

      <div class="m-sec">
        <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Education</div></div>'''
assert html.count(old) == 1, "anchor1 not found in 03"
html = html.replace(old, new)

# 2. Add Relevant Skills, Certifications, Achievements, References after Volunteer Experience
old2 = '''          <div class="entry"><div class="e-h" contenteditable="true">Organization Name</div><div class="e-t" contenteditable="true">Volunteer, 2022 - 2023</div><div class="e-t" contenteditable="true">Helped organize community activities and assisted with daily tasks of the team.</div></div>
        </div>
      </div>
    </div>
  </div>
</div>'''
new2 = '''          <div class="entry"><div class="e-h" contenteditable="true">Organization Name</div><div class="e-t" contenteditable="true">Volunteer, 2022 - 2023</div><div class="e-t" contenteditable="true">Helped organize community activities and assisted with daily tasks of the team.</div></div>
        </div>
      </div>

      <div class="m-sec">
        <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Relevant Skills</div></div>
        <div class="m-body">
          <div class="e-t" contenteditable="true">Communication</div>
          <div class="e-t" contenteditable="true">Teamwork</div>
          <div class="e-t" contenteditable="true">Problem Solving</div>
          <div class="e-t" contenteditable="true">Time Management</div>
        </div>
      </div>

      <div class="m-sec">
        <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Certifications / Training</div></div>
        <div class="m-body">
          <div class="e-t" contenteditable="true">Google Digital Marketing Certification</div>
          <div class="e-t" contenteditable="true">Basic Graphic Design (Online Course)</div>
        </div>
      </div>

      <div class="m-sec">
        <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Achievements / Activities</div></div>
        <div class="m-body">
          <div class="e-t" contenteditable="true">Dean's List (2022 - 2023)</div>
          <div class="e-t" contenteditable="true">Member, Student Organization (2021 - 2024)</div>
        </div>
      </div>

      <div class="m-sec">
        <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">References</div></div>
        <div class="m-body">
          <div class="e-t" contenteditable="true">Available upon request.</div>
        </div>
      </div>
    </div>
  </div>
</div>'''
assert html.count(old2) == 1, "anchor2 not found in 03"
html = html.replace(old2, new2)

# 3. Add quote block to sidebar
old3 = '''      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg><span contenteditable="true" data-f="address">Barangay, City, Philippines</span></div>
    </div>
  </div>'''
new3 = '''      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg><span contenteditable="true" data-f="address">Barangay, City, Philippines</span></div>
    </div>

    <div class="s-sec">
      <div class="quote" contenteditable="true">"Small steps create big progress."</div>
    </div>
  </div>'''
assert html.count(old3) == 1, "anchor3 not found in 03"
html = html.replace(old3, new3)

# 4. Add .quote CSS before closing </style>
old4 = "</style>"
new4 = ".quote{font-style:italic;font-size:13px;margin-top:20px;line-height:1.5;}\n</style>"
assert html.count(old4) == 1, "style close not found in 03"
html = html.replace(old4, new4, 1)

open(f, "w", encoding="utf-8").write(html)
print("Template 03 patched OK")

# ---------- TEMPLATE 06 ----------
f2 = "app/src/main/assets/templates/ai_template_more_06.html"
html2 = open(f2, encoding="utf-8").read()

old5 = '''          <ul class="bul">
            <li contenteditable="true">Contributed ideas for digital marketing campaigns for raising brand awareness</li>
            <li contenteditable="true">Organizes all social media posts for the editorial department</li>
          </ul>
        </div>
      </div>
    </div>
  </div>'''
new5 = '''          <ul class="bul">
            <li contenteditable="true">Contributed ideas for digital marketing campaigns for raising brand awareness</li>
            <li contenteditable="true">Organizes all social media posts for the editorial department</li>
          </ul>
        </div>
      </div>

      <div class="r-sec">
        <div class="h" contenteditable="true">Certifications</div>
        <div class="sub" contenteditable="true">Google Digital Marketing Certification</div>
        <div class="sub" contenteditable="true">HubSpot Inbound Marketing Certification</div>
      </div>

      <div class="r-sec">
        <div class="h" contenteditable="true">Languages</div>
        <div class="sk-row"><span class="n" contenteditable="true">English</span><div class="track"><div class="fill" style="width:100%"></div></div></div>
        <div class="sk-row"><span class="n" contenteditable="true">Filipino</span><div class="track"><div class="fill" style="width:100%"></div></div></div>
        <div class="sk-row"><span class="n" contenteditable="true">Spanish</span><div class="track"><div class="fill" style="width:80%"></div></div></div>
      </div>

      <div class="r-sec">
        <div class="h" contenteditable="true">Achievements / Key Highlights</div>
        <div class="sub" contenteditable="true">Recognized as Top Performer (2021)</div>
        <div class="sub" contenteditable="true">Led a successful rebranding campaign, +300% followers in 6 months</div>
      </div>
    </div>
  </div>'''
assert html2.count(old5) == 1, "anchor not found in 06"
html2 = html2.replace(old5, new5)

open(f2, "w", encoding="utf-8").write(html2)
print("Template 06 patched OK")
