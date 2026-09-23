#!/bin/bash
set -e
cd ~/UR-BioRes

for N in 12 13; do
  FILE="app/src/main/assets/templates/ai_template_more_${N}.html"
  
  # Fix viewport meta tag to match Template 02's fixed-width pattern
  sed -i 's|<meta name="viewport" content="width=device-width, initial-scale=1.0">|<meta name="viewport" content="width=850, minimum-scale=0.1, maximum-scale=5, user-scalable=yes">|' "$FILE"
  
  # Add fixed width to body so it matches .page width (850px)
  sed -i 's|body {|body {\n    width: 850px;|' "$FILE"
  
  echo "Fixed: $FILE"
done

git add app/src/main/assets/templates/ai_template_more_12.html app/src/main/assets/templates/ai_template_more_13.html
git commit -m "Fix viewport/scaling on templates 12 and 13 to match Template 02 pattern"
git push
echo "Done! Pushed to GitHub."
