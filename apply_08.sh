#!/data/data/com.termux/files/usr/bin/bash
set -e

HTML_PATH="app/src/main/assets/templates/ai_template_more_08.html"
KT_PATH="app/src/main/java/com/saltech/urdocs/ui/screens/ResumeTemplateGalleryScreen.kt"

echo "==> Creating $HTML_PATH"
cat > "$HTML_PATH" << 'HTMLEOF'
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=794, minimum-scale=0.1, maximum-scale=5, user-scalable=yes">
<style>
  :root{
    --navy:#12294f;
    --navy-deep:#0a1a33;
    --gold:#c9a227;
    --ink:#1c2536;
    --text:#3a4256;
    --gray:#7c869c;
  }
  *{box-sizing:border-box;margin:0;padding:0;-webkit-print-color-adjust:exact;print-color-adjust:exact}
  html{background:#000}
  body{background:#000;width:794px;font-family:'Roboto','Helvetica Neue',Arial,sans-serif;color:var(--text)}
  [contenteditable]{outline:none}
  [contenteditable="true"]:focus{background:#fff3cd;outline:1px dashed #999}

  .page{position:relative;width:794px;min-height:1123px;background:#fff;overflow:hidden}

  /* ---------- HEADER / DIAGONALS ---------- */
  .header{position:relative;height:270px;padding:40px 48px 0 48px}
  .diag-navy{position:absolute;top:0;left:0;width:520px;height:230px;background:var(--navy);
    clip-path:polygon(0 0,78% 0,30% 100%,0 100%)}
  .diag-gold{position:absolute;top:0;left:0;width:560px;height:230px;background:var(--gold);
    clip-path:polygon(0 0,84% 0,0 46%);opacity:.95;z-index:1}
  .diag-navy2{position:absolute;top:0;left:0;width:500px;height:230px;background:var(--navy-deep);
    clip-path:polygon(0 0,72% 0,26% 100%,0 100%);z-index:2}

  .photo-ring{position:absolute;top:60px;right:56px;width:170px;height:170px;border-radius:50%;
    border:6px solid var(--gold);z-index:3;overflow:hidden;background:#dfe3f0}
  .photo-circle{width:100%;height:100%;border-radius:50%;object-fit:cover;
    background:#dfe3f0 url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'><path fill='%23b9c0d8' d='M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v4h16v-4c0-2.66-5.33-4-8-4z'/></svg>") center/cover no-repeat}

  .name-block{position:relative;z-index:3;margin-top:150px}
  .name-wrap{white-space:nowrap;overflow:hidden;font-weight:800;font-size:40px;color:var(--navy);letter-spacing:-.5px}
  .job{margin-top:4px;font-size:15px;font-weight:600;color:var(--text);letter-spacing:2px;text-transform:uppercase}

  /* ---------- BODY (2 COLUMNS) ---------- */
  .body{display:flex;padding:20px 48px 40px}
  .col-left{flex:0 0 250px;padding-right:24px}
  .col-right{flex:1;min-width:0;padding-left:24px;border-left:1px solid #e3e6ee}

  .l-sec{margin-bottom:24px}
  .l-title{font-size:16px;font-weight:800;color:var(--navy);letter-spacing:1px;text-transform:uppercase;margin-bottom:10px}
  .skill-list{list-style:none}
  .skill-list li{font-size:13px;line-height:1.9;padding-left:14px;position:relative}
  .skill-list li::before{content:"";position:absolute;left:0;top:8px;width:5px;height:5px;border-radius:50%;background:var(--gold)}

  .edu-item{margin-bottom:12px}
  .edu-date{font-size:12px;font-weight:700;color:var(--gold)}
  .edu-title{font-size:13px;font-weight:700;color:var(--ink);margin-top:2px}
  .edu-sub{font-size:12px;margin-top:2px}

  .ref-item{margin-bottom:10px}
  .ref-name{font-size:13px;font-weight:700;color:var(--ink)}
  .ref-sub{font-size:12px}

  .contact-item{font-size:12.5px;line-height:1.9}

  .r-quote{font-size:16px;font-weight:700;color:var(--ink);line-height:1.5;margin-bottom:14px}
  .r-summary{font-size:12.5px;line-height:1.7;margin-bottom:22px}

  .r-title{font-size:17px;font-weight:800;color:var(--navy);text-transform:uppercase;letter-spacing:1px;margin-bottom:14px;
    padding-bottom:6px;border-bottom:2px solid var(--gold)}

  .wexp-item{position:relative;padding-left:20px;margin-bottom:18px}
  .wexp-item::before{content:"";position:absolute;left:0;top:4px;width:9px;height:9px;border-radius:50%;background:var(--navy)}
  .wexp-item::after{content:"";position:absolute;left:4px;top:14px;bottom:-18px;width:1px;background:#d8dce6}
  .wexp-item:last-child::after{display:none}
  .wexp-date{font-size:12px;font-weight:700;color:var(--gold)}
  .wexp-role{font-size:14px;font-weight:700;color:var(--ink);margin-top:2px}
  .wexp-co{font-size:12.5px;font-weight:600;color:var(--navy)}
  .wexp-desc{font-size:12.5px;line-height:1.6;margin-top:4px}
</style>
</head>
<body>
<div class="page">

  <div class="header">
    <div class="diag-navy"></div>
    <div class="diag-gold"></div>
    <div class="diag-navy2"></div>
    <div class="photo-ring"><div class="photo-circle"></div></div>
    <div class="name-block">
      <div class="name-wrap" id="nameWrap"><span id="nameText" contenteditable="true" data-f="name">JHOANNA ROBLES</span></div>
      <div class="job" contenteditable="true" data-f="title">Account Executive</div>
    </div>
  </div>

  <div class="body">
    <div class="col-left">
      <div class="l-sec">
        <div class="l-title" contenteditable="true">Skills</div>
        <ul class="skill-list">
          <li contenteditable="true">Strategy development</li>
          <li contenteditable="true">Organisation</li>
          <li contenteditable="true">Public relations</li>
          <li contenteditable="true">Customer service</li>
          <li contenteditable="true">Innovative thinking</li>
          <li contenteditable="true">Negotiation skills</li>
        </ul>
      </div>

      <div class="l-sec">
        <div class="l-title" contenteditable="true">Education</div>
        <div class="edu-item">
          <div class="edu-date" contenteditable="true">2008 - 2011</div>
          <div class="edu-title" contenteditable="true">Bachelor of Business</div>
          <div class="edu-sub" contenteditable="true">Borcelle University</div>
        </div>
        <div class="edu-item">
          <div class="edu-date" contenteditable="true">2005 - 2007</div>
          <div class="edu-title" contenteditable="true">Certificate in Marketing</div>
          <div class="edu-sub" contenteditable="true">Fauget School of The Arts</div>
        </div>
      </div>

      <div class="l-sec">
        <div class="l-title" contenteditable="true">References</div>
        <div class="ref-item">
          <div class="ref-name" contenteditable="true">Jamie Chastain</div>
          <div class="ref-sub" contenteditable="true">Timmerman Industries</div>
          <div class="ref-sub" contenteditable="true">123-456-7890</div>
        </div>
        <div class="ref-item">
          <div class="ref-name" contenteditable="true">Claudia Alves</div>
          <div class="ref-sub" contenteditable="true">Thynk Unlimited</div>
          <div class="ref-sub" contenteditable="true">123-456-7890</div>
        </div>
      </div>

      <div class="l-sec">
        <div class="l-title" contenteditable="true">Contact</div>
        <div class="contact-item" contenteditable="true" data-f="phone">+123-456-7890</div>
        <div class="contact-item" contenteditable="true" data-f="email">hello@email.com</div>
        <div class="contact-item" contenteditable="true" data-f="address">123 Anywhere St., Any City</div>
      </div>
    </div>

    <div class="col-right">
      <div class="r-quote" contenteditable="true">In my role as Account Executive, I am responsible for developing and planning marketing campaigns and strategies to promote services, events and products.</div>
      <div class="r-summary" contenteditable="true">With over 10 years experience, my role involves liaising with clients, planning, organising events, advertising, PR and research. I specialise in building lasting client relationships and developing effective strategies.</div>

      <div class="r-title" contenteditable="true">Work Experience</div>

      <div class="wexp-item">
        <div class="wexp-date" contenteditable="true">2021 - current</div>
        <div class="wexp-role" contenteditable="true">Account Executive</div>
        <div class="wexp-co" contenteditable="true">Timmerman Industries</div>
        <div class="wexp-desc" contenteditable="true">My role involves liaising with clients, planning, organising events, advertising and PR. I specialise in building lasting client relationships and developing effective strategies.</div>
      </div>
      <div class="wexp-item">
        <div class="wexp-date" contenteditable="true">2016 - 2020</div>
        <div class="wexp-role" contenteditable="true">Account Executive</div>
        <div class="wexp-co" contenteditable="true">Thynk Unlimited</div>
        <div class="wexp-desc" contenteditable="true">My role involved liaising with clients, planning, organising events, advertising and PR. I specialise in building lasting client relationships and developing effective strategies.</div>
      </div>
      <div class="wexp-item">
        <div class="wexp-date" contenteditable="true">2011 - 2015</div>
        <div class="wexp-role" contenteditable="true">Junior Account Executive</div>
        <div class="wexp-co" contenteditable="true">Studio Shodwe</div>
        <div class="wexp-desc" contenteditable="true">My role involved liaising with clients, planning, organising events, advertising and PR. I specialise in building lasting client relationships and developing effective strategies.</div>
      </div>
    </div>
  </div>
</div>

<script>
(function(){
  var wrap=document.getElementById('nameWrap');
  function fit(){
    var size=40; wrap.style.fontSize=size+'px';
    while(wrap.scrollWidth>wrap.clientWidth && size>20){ size-=1; wrap.style.fontSize=size+'px'; }
  }
  document.getElementById('nameText').addEventListener('input',fit);
  window.addEventListener('load',fit); fit();
})();
</script>
</body>
</html>
HTMLEOF

echo "==> Patching enum (LayoutStyle)"
sed -i 's/NAVY_PHOTO, SAGE_MOON, PINK_FRAME }/NAVY_PHOTO, SAGE_MOON, PINK_FRAME, NAVY_DIAGONAL }/' "$KT_PATH"

echo "==> Patching resumeTemplates list"
sed -i '/TemplateInfo("ai_template_more_07.html", "07", Color(0xFFF5C8F0), Color(0xFFF7F5F6), LayoutStyle.PINK_FRAME),/a\    TemplateInfo("ai_template_more_08.html", "08", Color(0xFF12294F), Color(0xFFFFFFFF), LayoutStyle.NAVY_DIAGONAL),' "$KT_PATH"

echo "==> Patching TemplatePreview when-block"
sed -i '/LayoutStyle.PINK_FRAME -> PinkFramePreview(modifier)/a\        LayoutStyle.NAVY_DIAGONAL -> NavyDiagonalPreview(modifier)' "$KT_PATH"

echo "==> Appending NavyDiagonalPreview composable"
cat >> "$KT_PATH" << 'KTEOF'

@Composable
private fun NavyDiagonalPreview(modifier: Modifier = Modifier) {
    val navy = Color(0xFF12294F)
    val gold = Color(0xFFC9A227)
    val gray = Color(0xFF9AA0B0)
    Column(modifier = modifier.background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().height(46.dp)) {
            Box(modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight().background(navy))
            Box(
                modifier = Modifier.align(Alignment.TopEnd).padding(6.dp)
                    .size(30.dp).clip(CircleShape).background(Color(0xFFDFE3F0)).border(2.dp, gold, CircleShape)
            )
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(7.dp).background(navy))
            Spacer(Modifier.height(4.dp))
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(3.dp).background(gray))
            Spacer(Modifier.height(10.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(3.dp).background(navy))
                Spacer(Modifier.height(3.dp))
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}
KTEOF

echo "==> Verifying"
c1=$(grep -c "ai_template_more_08" "$KT_PATH")
c2=$(grep -c "NAVY_DIAGONAL" "$KT_PATH")
echo "ai_template_more_08 occurrences: $c1 (expect 1)"
echo "NAVY_DIAGONAL occurrences: $c2 (expect 4)"

if [ "$c1" -ne 1 ] || [ "$c2" -ne 4 ]; then
  echo "‼️  Verification failed — hindi commit/push. Check the file manually."
  exit 1
fi

echo "==> Committing and pushing"
git add "$HTML_PATH" "$KT_PATH"
git commit -m "Add Template 08 (Navy Diagonal, Jhoanna Robles style)"
git push

echo "✅ Done — Template 08 added, committed, pushed."
