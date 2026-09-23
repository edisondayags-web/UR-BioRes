import glob

MARKER = "AUTO-FIT-SCALE"

SCRIPT = '''
<!-- AUTO-FIT-SCALE -->
<script>
(function(){
  function fitToScreen(){
    var BUTTON_RESERVE = 160;
    var availableHeight = window.innerHeight - BUTTON_RESERVE;
    var contentHeight = document.body.scrollHeight;
    var scale = Math.min(1, availableHeight / contentHeight);
    if (scale < 0.5) scale = 0.5;
    document.body.style.transformOrigin = 'top center';
    document.body.style.transform = 'scale(' + scale + ')';
    document.documentElement.style.overflow = 'hidden';
    document.body.style.overflow = 'hidden';
  }
  window.addEventListener('load', fitToScreen);
  window.addEventListener('resize', fitToScreen);
})();
</script>
'''

files = glob.glob("app/src/main/assets/templates/*.html")
count = 0
skipped = 0
for f in files:
    html = open(f, encoding="utf-8").read()
    if MARKER in html:
        skipped += 1
        continue
    if "</body>" not in html:
        print(f"WARNING: no </body> in {f}, skipped")
        continue
    html = html.replace("</body>", SCRIPT + "</body>", 1)
    open(f, "w", encoding="utf-8").write(html)
    count += 1

print(f"Injected into {count} files. Skipped (already has marker): {skipped}. Total files scanned: {len(files)}")
