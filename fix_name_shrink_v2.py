import re
import os

TEMPLATE_DIR = "app/src/main/assets/templates"

NEW_SCRIPT = '''<script>
(function(){
  function getTargets(container){
    return Array.prototype.filter.call(container.children, function(c){ return c.tagName === 'SPAN'; });
  }
  function computeMaxWidth(container){
    var parent = container.parentElement;
    if(!parent) return container.clientWidth;
    var parentRect = parent.getBoundingClientRect();
    var containerRect = container.getBoundingClientRect();
    var available = parentRect.right - containerRect.left;
    if(!available || available <= 0){
      available = parent.clientWidth - (containerRect.left - parentRect.left);
    }
    return available - 2;
  }
  function shrinkToFit(container){
    var targets = getTargets(container);
    if(!targets.length) return;
    if(!container.dataset.baseFont){
      container.dataset.baseFont = parseFloat(getComputedStyle(targets[0]).fontSize) || 22;
    }
    var baseFont = parseFloat(container.dataset.baseFont);
    var minFont = 5;
    var maxWidth = computeMaxWidth(container);
    if(!maxWidth || maxWidth <= 0) return;
    var fs = baseFont;
    targets.forEach(function(t){ t.style.fontSize = fs + 'px'; });
    var guard = 0;
    while(container.scrollWidth > maxWidth && fs > minFont && guard < 120){
      fs -= 0.5;
      targets.forEach(function(t){ t.style.fontSize = fs + 'px'; });
      guard++;
    }
  }
  function init(){
    var container = document.querySelector('.name-row2, .name-row, .h-name, .name');
    if(!container) return;
    container.style.whiteSpace = 'nowrap';
    var targets = getTargets(container);
    shrinkToFit(container);
    targets.forEach(function(t){
      t.addEventListener('input', function(){ shrinkToFit(container); });
      t.addEventListener('keyup', function(){ shrinkToFit(container); });
    });
    var mo = new MutationObserver(function(){ shrinkToFit(container); });
    targets.forEach(function(t){ mo.observe(t, { characterData: true, subtree: true, childList: true }); });
    window.addEventListener('resize', function(){ shrinkToFit(container); });
    window.addEventListener('load', function(){ shrinkToFit(container); });
  }
  if(document.readyState === 'loading'){
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
</script>'''

package2_files = [f"{TEMPLATE_DIR}/ai_template_03.html"] + \
    [f"{TEMPLATE_DIR}/ai_template_03_v{i}.html" for i in range(1, 30)]

package5_files = [f"{TEMPLATE_DIR}/ai_template_p5_{i:02d}.html" for i in range(1, 31)]

all_files = package2_files + package5_files

script_pattern = re.compile(r'<script>(?:(?!</script>).)*?shrinkToFit(?:(?!</script>).)*?</script>', re.DOTALL)

fixed = 0
skipped_no_match = []
missing_files = []

for path in all_files:
    if not os.path.exists(path):
        missing_files.append(path)
        continue
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()

    new_content, count = script_pattern.subn(NEW_SCRIPT, content, count=1)

    if count == 0:
        skipped_no_match.append(path)
        continue

    with open(path, "w", encoding="utf-8") as f:
        f.write(new_content)
    fixed += 1

print(f"Fixed: {fixed} files")
if skipped_no_match:
    print(f"\nWalang nahanap na shrinkToFit script ({len(skipped_no_match)}):")
    for p in skipped_no_match:
        print(f"  {p}")
if missing_files:
    print(f"\nHindi nahanap na file ({len(missing_files)}):")
    for p in missing_files:
        print(f"  {p}")
