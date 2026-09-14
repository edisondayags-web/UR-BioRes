import re

path = "app/src/main/java/com/saltech/urdocs/ui/templates/TemplateExportMenu.kt"
with open(path) as f:
    content = f.read()

content = content.replace(
    "import com.google.android.gms.ads.interstitial.InterstitialAd\nimport com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback",
    "import com.google.android.gms.ads.rewarded.RewardedAd\nimport com.google.android.gms.ads.rewarded.RewardedAdLoadCallback\nimport com.google.android.gms.ads.LoadAdError"
)

content = content.replace(
    "var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }",
    "var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }"
)

old_load = '''LaunchedEffect(Unit) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3134240485602899/5274307709",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }'''

new_load = '''LaunchedEffect(Unit) {
        RewardedAd.load(
            context,
            "ca-app-pub-3134240485602899/3509642738",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }'''

content = content.replace(old_load, new_load)

old_click = '''if (activity != null && interstitialAd != null) {
                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null
                            proceedDownload()
                        }
                    }
                    interstitialAd?.show(activity)
                } else {
                    proceedDownload()
                }'''

new_click = '''if (activity != null && rewardedAd != null) {
                    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            rewardedAd = null
                        }
                    }
                    rewardedAd?.show(activity) { proceedDownload() }
                } else {
                    Toast.makeText(context, "Loading ad, please wait...", Toast.LENGTH_SHORT).show()
                }'''

content = content.replace(old_click, new_click)

with open(path, "w") as f:
    f.write(content)

print("Done")
