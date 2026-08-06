# UR Docs (Sal-Tech Software Services)

Skeleton project: Resume/Bio-Data/Gov't Forms/Letters maker app.
- 2x2 ID photo: ML Kit (TFLite-based, Google) face detection + selfie segmentation, offline.
- Letter generation: Gemini API via Firebase Cloud Function proxy (API key hindi kailanman
  nasa client/app -- server side lang, secure).

## SETUP SA TERMUX

1. `cd ~` tapos i-extract itong zip, o git clone kapag na-push mo na sa GitHub.
2. Kailangan mo gumawa ng Firebase project (kung wala ka pa):
   - Pumunta sa console.firebase.google.com
   - Add app > Android > package name: `com.saltech.urdocs`
   - I-download ang `google-services.json`, ilagay sa `app/google-services.json`
     (HUWAG i-commit ito sa public repo -- nasa .gitignore na dapat)
3. I-enable ang Anonymous Auth sa Firebase Console > Authentication > Sign-in method.

## SETUP NG GEMINI API + CLOUD FUNCTION

1. Kumuha ng Gemini API key: https://aistudio.google.com/apikey (libre, walang credit card)
2. I-install ang Firebase CLI (kung wala pa): `npm install -g firebase-tools`
3. `firebase login`
4. Sa loob ng project folder: `firebase init functions` (piliin yung existing `functions/` folder)
5. I-set ang secret:
   ```
   firebase functions:secrets:set GEMINI_API_KEY
   ```
   (i-paste yung Gemini API key mo kapag hiningi)
6. Deploy: `firebase deploy --only functions`

## GITHUB ACTIONS (APK Build)

1. Sa GitHub repo settings > Secrets and variables > Actions, gumawa ng secret:
   - `GOOGLE_SERVICES_JSON` = base64-encoded na content ng google-services.json
     (sa Termux: `base64 -w 0 app/google-services.json` tapos i-copy yung output)
2. Push sa `main` branch -- automatic na mag-build ang APK.
3. Makikita mo ang built APK sa "Actions" tab > yung workflow run > Artifacts.

## KNOWN TODOs (susunod na session)

- `ImageProxy.toBitmap()` sa SelfieCaptureScreen.kt -- kailangan pa ng tamang
  YUV_420_888 -> Bitmap conversion (o gamitin ang bundled utility kung meron
  sa bersyon ng camera-core na ginagamit).
- PDF export ng Resume/Bio-Data (may `pdf` skill na pwedeng gamitin sa server
  side o library gaya ng PdfDocument sa Android mismo).
- Gov't form PDF templates (SSS, Pag-IBIG forms) -- kailangan ng actual
  template files para ma-fill programmatically.
- Palitan yung placeholder hex colors sa `ui/theme/Color.kt` ng EXACT values
  mula sa UR Call theme file para consistent ang branding.
- selfie segmentation loop sa BackgroundHelper.kt ay per-pixel (functional
  pero pwede pang i-optimize gamit ang RenderScript/Bitmap.setPixels batch).
<img width="720" height="728" alt="1000054155" src="https://github.com/user-attachments/assets/1ef63c58-0752-48cb-b762-c5954e8ada62" />
