# FIX ZONE — Android Studio Project (Starter Build)

এই প্রজেক্টটি FIX ZONE-এর বর্তমান Easy Control / Admin / Security / Data & Backup / AI নাম পরিবর্তনের Preview-কে Android WebView অ্যাপের মধ্যে চালানোর জন্য তৈরি।

## APK তৈরি
1. Android Studio ইনস্টল করে এই `FixZoneAndroidProject` ফোল্ডারটি Open করুন।
2. Gradle Sync সম্পন্ন হতে দিন (Android Gradle Plugin 8.6.1 এবং SDK 35 ডাউনলোড লাগতে পারে)।
3. Build > Build Bundle(s) / APK(s) > Build APK(s) নির্বাচন করুন।
4. APK তৈরি হলে Android Studio-র notification থেকে Locate করে ফোনে ইনস্টল করুন।

## বর্তমান Build-এর সীমাবদ্ধতা
- এটি এখনো পূর্ণ ব্যবসা পরিচালনা অ্যাপ নয়; বর্তমান HTML Preview-টি Android অ্যাপে মোড়ানো হয়েছে।
- Face/Fingerprint-এর native Android BiometricPrompt এখনো যুক্ত হয়নি। Preview-তে শুধু সেটিংস/ব্যাখ্যা আছে।
- বাস্তব FIX AI API, Cloud Database/Sync, encrypted remote backup, supplier live stock, POS, inventory, repair workflow, SMS/WhatsApp, printer ও payment integration এখনো যুক্ত নয়।
- Preview-র localStorage ডেটা কেবল এই অ্যাপের স্থানীয় WebView storage-এ থাকে; এটিকে নিরাপদ Cloud Backup বলা যাবে না।
- Release APK প্রকাশের আগে Android SDK দিয়ে build, বাস্তব ডিভাইসে পরীক্ষা, signing key তৈরি এবং security review আবশ্যক।

## নিরাপত্তা
এই starter build-এ কোনো Admin password বা customer data ডিফল্টভাবে রাখা হয়নি। বাস্তব customer data ব্যবহারের আগে authentication, authorization, encrypted storage, backup/restore এবং audit logging backend-এ বাস্তবায়ন করতে হবে।
