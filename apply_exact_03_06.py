import re

def replace_body(path, new_body):
    html = open(path, encoding="utf-8").read()
    html = re.sub(r"<body>.*</body>", new_body, html, flags=re.DOTALL)
    return html

def add_css(html, extra_css):
    return html.replace("</style>", extra_css + "\n</style>", 1)

# ========== TEMPLATE 03 ==========
f3 = "app/src/main/assets/templates/ai_template_more_03.html"

body3 = '''<body>
<div class="page">

  <div class="side">
    <div class="photo-circle"></div>

    <div class="s-sec">
      <div class="s-title" contenteditable="true">Contact Me</div>
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/></svg><span contenteditable="true" data-f="phone">0912 345 6789</span></div>
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg><span contenteditable="true" data-f="email">hello@email.com</span></div>
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg><span contenteditable="true" data-f="address">Barangay, City, Philippines</span></div>
    </div>

    <div class="s-sec">
      <div class="s-title" contenteditable="true">About Me</div>
      <div class="profile" contenteditable="true">I am a hardworking and motivated student with a strong interest in learning, technology, and personal growth. I am eager to apply my skills, gain real-world experience, and contribute positively to any team.</div>
    </div>

    <div class="s-sec">
      <div class="s-title" contenteditable="true">Languages</div>
      <div class="lang-row"><span contenteditable="true">English</span><div class="dots"><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot"></span></div></div>
      <div class="lang-row"><span contenteditable="true">Filipino</span><div class="dots"><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span></div></div>
      <div class="lang-row"><span contenteditable="true">Spanish</span><div class="dots"><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot"></span><span class="dot"></span></div></div>
    </div>

    <div class="s-sec">
      <div class="s-title" contenteditable="true">Availability</div>
      <div class="avail" contenteditable="true">Flexible / Part-time. Available for internship, part-time, or entry-level opportunities.</div>
    </div>

    <div class="s-sec">
      <div class="quote" contenteditable="true">"Small steps create big progress."</div>
    </div>
  </div>

  <div class="main">
    <div class="hero">
      <div class="name" id="nameText" contenteditable="true" data-f="name">Olivia Wilson</div>
      <div class="job" contenteditable="true" data-f="title">Student</div>
    </div>

    <div class="content">
      <div class="cols">
        <div class="col-l">
          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Career Objective</div></div>
            <div class="m-body">
              <div class="e-t" contenteditable="true">To obtain an internship or entry-level position where I can apply my skills, learn from experienced professionals, and contribute to the growth of the organization while building my career in the digital and creative field.</div>
            </div>
          </div>

          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Education</div></div>
            <div class="m-body">
              <div class="entry"><div class="e-h" contenteditable="true">Bachelor of Science in Business Administration</div><div class="e-t" contenteditable="true">Fauget University</div><div class="e-t" contenteditable="true">2020 - 2024</div></div>
              <div class="entry"><div class="e-h" contenteditable="true">Senior High School</div><div class="e-t" contenteditable="true">School Name</div><div class="e-t" contenteditable="true">2018 - 2020</div></div>
            </div>
          </div>

          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Volunteer Experience</div></div>
            <div class="m-body">
              <div class="entry"><div class="e-h" contenteditable="true">Organization Name</div><div class="e-t" contenteditable="true">Volunteer, 2022 - 2023</div><div class="e-t" contenteditable="true">Helped organize community activities and assisted with daily tasks of the team.</div></div>
            </div>
          </div>

          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">References</div></div>
            <div class="m-body">
              <div class="e-t" contenteditable="true">Available upon request.</div>
              <div class="e-t" contenteditable="true">I can provide references from professors, mentors, or previous work/volunteer supervisors.</div>
            </div>
          </div>
        </div>

        <div class="col-r">
          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Relevant Skills</div></div>
            <div class="m-body">
              <div class="e-t" contenteditable="true">Communication</div>
              <div class="e-t" contenteditable="true">Teamwork</div>
              <div class="e-t" contenteditable="true">Problem Solving</div>
              <div class="e-t" contenteditable="true">Time Management</div>
              <div class="e-t" contenteditable="true">Adaptability</div>
              <div class="e-t" contenteditable="true">Creative Thinking</div>
            </div>
          </div>

          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Computer Skills</div></div>
            <div class="m-body">
              <div class="e-t" contenteditable="true">Typing speed: 60 WPM</div>
              <div class="e-t" contenteditable="true">MS Word / Excel / PowerPoint</div>
              <div class="e-t" contenteditable="true">Google Workspace</div>
              <div class="e-t" contenteditable="true">Basic Web Design</div>
              <div class="e-t" contenteditable="true">Spreadsheet & Data Entry</div>
            </div>
          </div>

          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Certifications / Training</div></div>
            <div class="m-body">
              <div class="e-t" contenteditable="true">Google Digital Marketing Certification (Google)</div>
              <div class="e-t" contenteditable="true">HubSpot Inbound Marketing Certification</div>
              <div class="e-t" contenteditable="true">SEMrush SEO Fundamentals Certificate</div>
              <div class="e-t" contenteditable="true">Basic Graphic Design (Online Course)</div>
            </div>
          </div>

          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Achievements / Activities</div></div>
            <div class="m-body">
              <div class="e-t" contenteditable="true">Dean's List (2022 - 2023)</div>
              <div class="e-t" contenteditable="true">Completed a Digital Marketing Workshop</div>
              <div class="e-t" contenteditable="true">Member, Student Organization (2021 - 2024)</div>
              <div class="e-t" contenteditable="true">Top 10 in School Marketing Competition (2022)</div>
            </div>
          </div>

          <div class="m-sec">
            <div class="m-head"><span class="arrow"></span><div class="m-title" contenteditable="true">Career Availability</div></div>
            <div class="m-body">
              <div class="e-t" contenteditable="true">Open to internship, part-time, or full-time opportunities. Willing to learn, grow, and adapt to new challenges.</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
(function(){
  var n=document.getElementById('nameText');
  function fit(){
    var s=46; n.style.fontSize=s+'px';
    while(n.scrollHeight>s*1.15*3+2 && s>20){ s-=1; n.style.fontSize=s+'px'; }
  }
  n.addEventListener('input',fit);
  window.addEventListener('load',fit); fit();
})();
</script>
</body>'''

css3 = '''  .cols{display:flex;gap:36px}
  .col-l{flex:1.15;min-width:0}
  .col-r{flex:1;min-width:0}
  .lang-row{display:flex;justify-content:space-between;align-items:center;font-size:13px;margin-bottom:10px}
  .dots{display:flex;gap:4px}
  .dot{width:8px;height:8px;border-radius:50%;background:rgba(255,255,255,.35)}
  .dot.on{background:#fff}
  .avail{font-size:13px;line-height:1.6}'''

html3 = replace_body(f3, body3)
if ".cols{" not in html3:
    html3 = add_css(html3, css3)
open(f3, "w", encoding="utf-8").write(html3)
print("Template 03 rebuilt OK")

# ========== TEMPLATE 06 ==========
f6 = "app/src/main/assets/templates/ai_template_more_06.html"

body6 = '''<body>
<div class="page">
  <div class="deco a"></div>
  <div class="deco b"></div>

  <div class="top">
    <div class="left">
      <div class="moon"><div class="photo-circle"></div></div>
      <div class="name" id="nameText" contenteditable="true" data-f="name">Olivia Wilson</div>
      <div class="role" contenteditable="true" data-f="title">Digital Marketer</div>

      <div class="l-sec">
        <div class="l-h" contenteditable="true">Professional Summary</div>
        <div class="about" contenteditable="true">Results-driven Digital Marketer with 7+ years of experience in digital marketing, branding, and business strategy across media and entertainment industries. Skilled in creating data-driven campaigns, optimizing online presence, and driving engagement through creative content and strategic planning.</div>
      </div>

      <div class="l-sec">
        <div class="l-h" contenteditable="true">Certifications</div>
        <div class="sub" contenteditable="true">Google Digital Marketing & E-commerce Professional Certificate (Google)</div>
        <div class="sub" contenteditable="true">HubSpot Inbound Marketing Certification</div>
        <div class="sub" contenteditable="true">Meta Social Media Marketing Certification</div>
        <div class="sub" contenteditable="true">SEMrush SEO Fundamentals Certificate</div>
      </div>

      <div class="l-sec">
        <div class="l-h" contenteditable="true">Languages</div>
        <div class="lang-row"><span contenteditable="true">English</span><div class="dots"><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span></div></div>
        <div class="lang-row"><span contenteditable="true">Filipino</span><div class="dots"><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span></div></div>
        <div class="lang-row"><span contenteditable="true">Spanish</span><div class="dots"><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot on"></span><span class="dot"></span></div></div>
      </div>
    </div>

    <div class="right">
      <div class="r-sec">
        <div class="h" contenteditable="true">Education</div>
        <div class="ttl" contenteditable="true">Bachelor of Design</div>
        <div class="sub" contenteditable="true">Fauget University | 2012-2016</div>
        <div class="sub" contenteditable="true">Major in Digital Arts and Multimedia</div>
      </div>

      <div class="r-sec">
        <div class="h" contenteditable="true">Experience</div>
        <div class="job">
          <div class="ttl" contenteditable="true">Digital Marketing Lead</div>
          <div class="sub" contenteditable="true">Studio Shodwe | 2018-present</div>
          <ul class="bul">
            <li contenteditable="true">Created and managed digital campaigns for top clients, increasing brand awareness and engagement by 40% on average</li>
            <li contenteditable="true">Increased two clients' digital presence and customer interaction by 200% through integrated social media and content strategies</li>
            <li contenteditable="true">Approved all content to be posted on social media, ensuring brand consistency and compliance</li>
          </ul>
        </div>
        <div class="job">
          <div class="ttl" contenteditable="true">Digital Marketer</div>
          <div class="sub" contenteditable="true">Larana, Inc | 2016-2018</div>
          <ul class="bul">
            <li contenteditable="true">Contributed ideas for digital marketing campaigns for raising brand awareness and lead generation</li>
            <li contenteditable="true">Organized all social media posts for the editorial department, improving engagement by 35%</li>
            <li contenteditable="true">Assisted in SEO optimization and email marketing initiatives, resulting in increased website traffic</li>
          </ul>
        </div>
      </div>

      <div class="r-sec">
        <div class="h" contenteditable="true">Achievements / Key Highlights</div>
        <ul class="bul">
          <li contenteditable="true">Recognized as Top Performer (2021) for exceeding campaign goals by 150%</li>
          <li contenteditable="true">Led a successful rebranding campaign that increased social media followers by 300% in 6 months</li>
          <li contenteditable="true">Received "Creative Excellence" award for a viral content campaign (2020)</li>
        </ul>
      </div>
    </div>
  </div>

  <div class="box">
    <div class="contact">
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg><div class="t" contenteditable="true" data-f="email">hello@email.com</div></div>
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/></svg><div class="t" contenteditable="true" data-f="phone">+123-456-7890</div></div>
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg><div class="t" contenteditable="true" data-f="address">123 Anywhere St., Any City</div></div>
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M16.36 14c.08-.66.14-1.32.14-2 0-.68-.06-1.34-.14-2h3.38c.16.64.26 1.31.26 2s-.1 1.36-.26 2m-5.15 5.56c.6-1.11 1.06-2.31 1.38-3.56h2.95a8.03 8.03 0 0 1-4.33 3.56M14.34 14H9.66c-.1-.66-.16-1.32-.16-2 0-.68.06-1.35.16-2h4.68c.09.65.16 1.32.16 2 0 .68-.07 1.34-.16 2M12 19.96c-.83-1.2-1.5-2.53-1.91-3.96h3.82c-.41 1.43-1.08 2.76-1.91 3.96M8 8H5.08A7.923 7.923 0 0 1 9.4 4.44C8.8 5.55 8.35 6.75 8 8m-2.92 8H8c.35 1.25.8 2.45 1.4 3.56A8.008 8.008 0 0 1 5.08 16m-.82-2C4.1 13.36 4 12.69 4 12s.1-1.36.26-2h3.38c-.08.66-.14 1.32-.14 2 0 .68.06 1.34.14 2M12 4.03c.83 1.2 1.5 2.54 1.91 3.97h-3.82c.41-1.43 1.08-2.77 1.91-3.97M18.92 8h-2.95a15.65 15.65 0 0 0-1.38-3.56c1.84.63 3.37 1.9 4.33 3.56M12 2C6.47 2 2 6.5 2 12c0 5.53 4.47 10 10 10s10-4.47 10-10c0-5.5-4.47-10-10-10z"/></svg><div class="t" contenteditable="true" data-f="website">www.yourwebsite.com</div></div>
      <div class="c-row"><svg viewBox="0 0 24 24"><path d="M19 3a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h14m-.5 15.5v-5.3a3.26 3.26 0 0 0-3.26-3.26c-.85 0-1.84.52-2.32 1.3v-1.11h-2.79v8.37h2.79v-4.93c0-.77.62-1.4 1.39-1.4a1.4 1.4 0 0 1 1.4 1.4v4.93h2.79M6.88 8.56a1.68 1.68 0 0 0 1.68-1.68c0-.93-.75-1.69-1.68-1.69a1.69 1.69 0 0 0-1.69 1.69c0 .93.76 1.68 1.69 1.68m1.39 9.94v-8.37H5.5v8.37h2.77z"/></svg><div class="t" contenteditable="true" data-f="linkedin">linkedin.com/in/oliviawilson</div></div>
    </div>
    <div class="skills">
      <div class="sk-h" contenteditable="true">Skills</div>
      <div class="sk-row"><span class="n" contenteditable="true">Market Strategy</span><div class="track"><div class="fill" style="width:80%"></div></div></div>
      <div class="sk-row"><span class="n" contenteditable="true">Social Media Marketing</span><div class="track"><div class="fill" style="width:95%"></div></div></div>
      <div class="sk-row"><span class="n" contenteditable="true">Content Creation</span><div class="track"><div class="fill" style="width:85%"></div></div></div>
      <div class="sk-row"><span class="n" contenteditable="true">SEO & Analytics</span><div class="track"><div class="fill" style="width:80%"></div></div></div>
      <div class="sk-row"><span class="n" contenteditable="true">Project Management</span><div class="track"><div class="fill" style="width:90%"></div></div></div>
    </div>
  </div>
</div>

<script>
(function(){
  var n=document.getElementById('nameText');
  function fit(){
    var s=56; n.style.fontSize=s+'px';
    while(n.scrollHeight>s*1.3*2+2 && s>22){ s-=1; n.style.fontSize=s+'px'; }
  }
  n.addEventListener('input',fit);
  window.addEventListener('load',fit); fit();
})();
document.querySelectorAll('.track').forEach(function(t){
  t.addEventListener('click',function(e){
    var r=t.getBoundingClientRect();
    var p=Math.round(((e.clientX-r.left)/r.width)*10)*10;
    p=Math.max(10,Math.min(100,p));
    t.querySelector('.fill').style.width=p+'%';
  });
});
</script>
</body>'''

css6 = '''  .l-sec{margin-top:26px}
  .l-h{font-size:16px;font-weight:800;letter-spacing:2px;color:var(--ink);text-transform:uppercase;margin-bottom:10px}
  .lang-row{display:flex;justify-content:space-between;align-items:center;font-size:13px;margin-bottom:8px;color:var(--text)}
  .dots{display:flex;gap:4px}
  .dot{width:8px;height:8px;border-radius:50%;background:rgba(0,0,0,.15)}
  .dot.on{background:var(--ink)}'''

html6 = replace_body(f6, body6)
if ".l-sec{" not in html6:
    html6 = add_css(html6, css6)
open(f6, "w", encoding="utf-8").write(html6)
print("Template 06 rebuilt OK")
