import glob

SCRIPT = '''
<script>
/* AUTO-FIT: stretches spacing if content is shorter than the bond paper; does nothing if it already fits */
(function(){
  function autoFit(){
    var page = document.querySelector('.page');
    if(!page) return;
    var target = parseInt(getComputedStyle(page).minHeight) || 1123;
    var actual = page.scrollHeight;
    if(actual >= target * 0.95) return;
    var deficit = target - actual;
    var candidates = Array.prototype.filter.call(page.querySelectorAll('*'), function(el){
      return /-sec\\b|\\bjob\\b|\\bentry\\b|\\bitem\\b|exp-item|side-block/i.test(el.className || '');
    });
    if(candidates.length === 0) return;
    var extra = Math.min(Math.floor(deficit / candidates.length), 40);
    if(extra <= 0) return;
    candidates.forEach(function(el){
      var mb = parseInt(getComputedStyle(el).marginBottom) || 0;
      el.style.marginBottom = (mb + extra) + 'px';
    });
  }
  window.addEventListener('load', autoFit);
})();
</script>
'''

files = glob.glob("app/src/main/assets/templates/*.html")
count = 0
for f in files:
    html = open(f, encoding="utf-8").read()
    if "AUTO-FIT" in html:
        continue  # already injected, skip
    if "</body>" not in html:
        continue
    html = html.replace("</body>", SCRIPT + "</body>", 1)
    open(f, "w", encoding="utf-8").write(html)
    count += 1

print(f"Injected auto-fit into {count} of {len(files)} template files")
