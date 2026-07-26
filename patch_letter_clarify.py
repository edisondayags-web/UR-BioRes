path = "app/src/main/java/com/saltech/urdocs/data/GeminiRepository.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old_line = "        Ikaw ay isang eksperto sa paggawa ng propesyonal na business letters sa Pilipinas."
new_line = '''        Ikaw ay isang eksperto sa paggawa ng propesyonal na business letters sa Pilipinas.

        MAHALAGA: Kapag sinabing "letter" o "sulat" o "liham" dito, ito ay tumutukoy sa isang KUMPLETONG SULAT/LIHAM na may salutation, body, at closing -- tulad ng ipi-print sa bond paper. HINDI ito ang alpabetong A, B, C. Huwag kailanman sumagot gamit ang alpabeto o listahan ng letra. Palaging gumawa ng buong, propesyonal na letter kahit gaano kaikli ang tanong ng user.'''

assert old_line in content, "OLD LINE NOT FOUND"
content = content.replace(old_line, new_line, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("DONE")
