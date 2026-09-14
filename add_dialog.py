import re

path = "app/src/main/java/com/saltech/urdocs/ui/templates/TemplateExportMenu.kt"
with open(path) as f:
    content = f.read()

content = content.replace(
    "var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }",
    "var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }\n    var showWatchAdDialog by remember { mutableStateOf(false) }"
)

old_click = '''DropdownMenuItem(text = { Text("Download") }, onClick = {
                expanded = false
                val activity = context as? android.app.Activity
                fun proceedDownload() {
                    scope.launch {
                        val bmp = captureBitmap()
                        saveBitmapToGallery(context, bmp, resumeName)
                    }
                }
                if (activity != null && rewardedAd != null) {
                    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            rewardedAd = null
                        }
                    }
                    rewardedAd?.show(activity) { proceedDownload() }
                } else {
                    Toast.makeText(context, "Loading ad, please wait...", Toast.LENGTH_SHORT).show()
                }
            })'''

new_click = '''DropdownMenuItem(text = { Text("Download") }, onClick = {
                expanded = false
                showWatchAdDialog = true
            })'''

content = content.replace(old_click, new_click)

old_showqr = "    if (showQr) {"
new_dialog = '''    if (showWatchAdDialog) {
        AlertDialog(
            onDismissRequest = { showWatchAdDialog = false },
            title = { Text("Watch ad to download") },
            text = { Text("Please watch the full ad to unlock this template download. Closing the ad early will cancel the download.") },
            confirmButton = {
                TextButton(onClick = {
                    showWatchAdDialog = false
                    val activity = context as? android.app.Activity
                    fun proceedDownload() {
                        scope.launch {
                            val bmp = captureBitmap()
                            saveBitmapToGallery(context, bmp, resumeName)
                        }
                    }
                    if (activity != null && rewardedAd != null) {
                        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                rewardedAd = null
                            }
                        }
                        rewardedAd?.show(activity) { proceedDownload() }
                    } else {
                        Toast.makeText(context, "Loading ad, please wait...", Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Watch") }
            },
            dismissButton = {
                TextButton(onClick = { showWatchAdDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showQr) {'''

content = content.replace(old_showqr, new_dialog)

with open(path, "w") as f:
    f.write(content)

print("Done 1")
