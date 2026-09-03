#!/usr/bin/env python3
import sys

path = "app/src/main/java/com/saltech/urdocs/ui/screens/AiTemplateScreen.kt"

with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''private suspend fun getDocumentHeightPx(webView: WebView, density: Float): Int =
    suspendCancellableCoroutine { cont ->
        webView.evaluateJavascript("document.body.scrollHeight.toString()") { result ->
            val cssHeight = result?.replace("\\"", "")?.toFloatOrNull() ?: 0f
            if (cont.isActive) cont.resume((cssHeight * density).toInt()) { }
        }
    }'''

new = '''private suspend fun neutralizeViewportHeight(webView: WebView): Unit =
    suspendCancellableCoroutine { cont ->
        // 100vh / min-height:100vh sa CSS ay sumusukat base sa screen height ng WebView,
        // hindi sa totoong laman -- kaya mali yung scrollHeight na nababasa natin pag
        // may ganyang CSS. I-neutralize muna bago tayo sumukat.
        val js = """
            (function(){
              var s = document.createElement('style');
              s.innerHTML = 'html, body { height: auto !important; min-height: auto !important; }';
              document.head.appendChild(s);
            })();
        """.trimIndent()
        webView.evaluateJavascript(js) {
            if (cont.isActive) cont.resume(Unit) { }
        }
    }

private suspend fun getDocumentHeightPx(webView: WebView, density: Float): Int =
    suspendCancellableCoroutine { cont ->
        webView.evaluateJavascript("document.body.scrollHeight.toString()") { result ->
            val cssHeight = result?.replace("\\"", "")?.toFloatOrNull() ?: 0f
            if (cont.isActive) cont.resume((cssHeight * density).toInt()) { }
        }
    }'''

old2 = '''private suspend fun captureFullWebView(webView: WebView, density: Float): Bitmap {
    val originalHeight = webView.height
    val originalLayerType = webView.layerType

    val docHeight = getDocumentHeightPx(webView, density).coerceAtLeast(originalHeight)'''

new2 = '''private suspend fun captureFullWebView(webView: WebView, density: Float): Bitmap {
    val originalHeight = webView.height
    val originalLayerType = webView.layerType

    neutralizeViewportHeight(webView)
    delay(50)
    val docHeight = getDocumentHeightPx(webView, density).coerceAtLeast(1)'''

edits = [("getDocumentHeightPx block", old, new), ("captureFullWebView start", old2, new2)]

errors = []
for name, o, n in edits:
    c = content.count(o)
    if c == 0:
        errors.append(f"NO MATCH for [{name}]")
    elif c > 1:
        errors.append(f"{c} MATCHES for [{name}]")

if errors:
    print("Walang nagalaw sa file mo. Mga problema:")
    for e in errors:
        print(" - " + e)
    sys.exit(1)

for name, o, n in edits:
    content = content.replace(o, n)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("Done! Na-fix na yung height-capture bug -- isang fix, apektado lahat ng 150 templates.")
