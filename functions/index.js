const { onCall, HttpsError } = require("firebase-functions/v2/https");
const { defineSecret } = require("firebase-functions/params");

// Deploy time: firebase functions:secrets:set GEMINI_API_KEY
const GEMINI_API_KEY = defineSecret("GEMINI_API_KEY");

const LETTER_PROMPTS = {
  LEAVE: "Sumulat ng propesyonal na Leave Letter (Tagalog-English business tone)",
  EXCUSE: "Sumulat ng Excuse Letter para sa absence",
  RESIGNATION: "Sumulat ng propesyonal na Resignation Letter",
  GOVT_SSS: "Sumulat ng formal na letter/request para sa SSS",
  GOVT_PAGIBIG: "Sumulat ng formal na letter/request para sa Pag-IBIG",
  CUSTOM: "Sumulat ng propesyonal na business letter"
};

exports.generateLetter = onCall({ secrets: [GEMINI_API_KEY] }, async (request) => {
  const data = request.data || {};
  const { letterType, fullName, position, company, reason, dateNeeded, extraDetails } = data;

  if (!fullName) {
    throw new HttpsError("invalid-argument", "Kailangan ng fullName.");
  }

  const instruction = LETTER_PROMPTS[letterType] || LETTER_PROMPTS.CUSTOM;
  const prompt = `${instruction}.
Pangalan: ${fullName}
Position: ${position || "N/A"}
Company/Office: ${company || "N/A"}
Rason: ${reason || "N/A"}
Petsa: ${dateNeeded || "N/A"}
Extra details: ${extraDetails || "wala"}

Gumawa ng kumpletong letter, propesyonal ang tono, ready to print/send. Filipino business letter format.`;

  const apiKey = GEMINI_API_KEY.value();
  const url = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}`;

  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      contents: [{ parts: [{ text: prompt }] }]
    })
  });

  if (!response.ok) {
    const errText = await response.text();
    throw new HttpsError("internal", `Gemini API error: ${errText}`);
  }

  const result = await response.json();
  const letterText =
    result?.candidates?.[0]?.content?.parts?.[0]?.text ||
    "Walang na-generate na letter. Subukan ulit.";

  return { letterText };
});
const { onCall } = require("firebase-functions/v2/https");
const { defineSecret } = require("firebase-functions/params");
const GEMINI_API_KEY = defineSecret("GEMINI_API_KEY");

exports.enhance2x2Photo = onCall({ secrets: [GEMINI_API_KEY] }, async (request) => {
  const { imageBase64 } = request.data;
  if (!imageBase64) throw new Error("Missing imageBase64");

  const prompt = "Enhance this ID photo: studio-quality lighting, clean pure " +
    "white background, sharp and clear focus on the face. Keep the same " +
    "person, pose, and framing exactly as-is. Output only the edited image.";

  const res = await fetch(
    `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-image:generateContent?key=${GEMINI_API_KEY.value()}`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        contents: [{ parts: [
          { text: prompt },
          { inline_data: { mime_type: "image/jpeg", data: imageBase64 } }
        ]}]
      })
    }
  );

  const json = await res.json();
  const parts = json?.candidates?.[0]?.content?.parts ?? [];
  const imagePart = parts.find((p) => p.inlineData || p.inline_data);
  const outData = imagePart?.inlineData?.data ?? imagePart?.inline_data?.data;
  if (!outData) throw new Error("Gemini did not return an image");

  return { imageBase64: outData };
});
