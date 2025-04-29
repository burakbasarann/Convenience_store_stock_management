# 📦 Convenience Store Stock Management App

Bu uygulama Market stok yönetimi amacıyla geliştirilmiş olup ürün takibi, tedarikçi yönetimi, satış/kayıt geçmişi gibi birçok özelliği içerisinde barındırır.

## 🚀 Kurulum
https://drive.google.com/file/d/1wGqiVF8-wPSa0n3Rw48Q2C6t1F1OG-OK/view?usp=sharing
Apk Dosyası EKte ki Linkte Mevcuttur.

## 🛠️ Kullanılan Teknolojiler

- Kotlin
- MVVM Architecture
- Room DB
- LiveData & Flow
- Hilt (Dependency Injection)
- Navigation Component
- Material Design
- Coroutines
- Jetpack Libraries
- Tek Aktivite - Çoklu Fragment yapısı (Navigation Graph)

## 📱 Uygulama Özellikleri

### Temel Özellikler
- ✅ Splash Screen
- ✅ Login ekranı (RoomDb ile bu userlar filtrelendi - Her kullanıcının yaptığı işlemler, ürünler ve tedarikçiler kullanıcıya özgü gelmektedir.)
- ✅ Dashboard: düşük stok uyarıları, son işlemler
- ✅ Ürün Listesi: Search ve alfabeye, fiyata göre sıralama
- ✅ Ürün Ekle / Düzenle
- ✅ Tedarikçi Listesi & Detayı : Search ve filtreleme özellikleri
- ✅ Tedarikçi Ekle / Düzenle
- ✅ İşlem Geçmişi (Stok Girişi / Satış / Pdf ve Cvs Çıktısı Alma) 
- ✅ Stok Seviyesi Takibi ve Notification
- ✅ Hata yönetimi ve kullanıcı geri bildirimi
- ✅ Veri doğrulama
- ✅ Navigation ile ekran geçişleri
- ✅ Responsive UI & Material Design uyumluluğu

### Bonus Özellikler

- ✅ İşlem geçmişini PDF ve CSV olarak dışa aktarma
- ✅ Offline çalışma desteği
- ✅ Unit Test
- ✅ Bildirim sistemi (düşük stok vs.)

## 🧠 Mimarî

- MVVM yapısı kullanıldı
- Repository Pattern ile veri kaynakları soyutlandı
- Dependency Injection: Hilt
- Reactive UI: LiveData ve Flow
- Room ile local veritabanı yönetimi


## 📤 Veri Dışa Aktarma

İşlem geçmişi, `.pdf` ve `.csv` formatında dışa aktarılabilir. Bu dosya cihazın public dizinine kaydedilir.

## 📡 Offline Desteği

Uygulama internet bağlantısı olmadan çalışabilir. Bağlantı geldiğinde veriler senkronize edilir.

## 🔔 Bildirimler

Stok seviyesi minimum seviyenin altına düştüğünde kullanıcıya bildirim gönderilir.


## 📂 Proje Yapısı
├── data  
  ├── local/dao 
  ├── model 
├── di 
├── repository 
  ├── product
  ├── supplier  
  ├── transaction 
  ├── user 
├── ui 
  ├── adapter 
  ├── base 
  ├── dashboard
  ├── login 
  ├── products 
    └── addeditproduct 
  ├── suppliers 
    └── addeditsupplier 
  ├── transaction 
├── util

## 📚 Kullanılan Kütüphaneler

| Kategori         | Kütüphaneler |
|------------------|--------------|
| **Temel Android** | `androidx.core.ktx`, `androidx.appcompat`, `androidx.activity`, `androidx.constraintlayout`, `material` |
| **Splash Ekranı** | `androidx.splashscreen` |
| **Animasyon** | `lottie` |
| **Dependency Injection** | `Hilt` (`hilt-android`, `hilt-compiler`) |
| **Veritabanı** | `Room` (`room-runtime`, `room-ktx`, `room-compiler`) |
| **Navigation** | `androidx.navigation.fragment`, `androidx.navigation.ui` |
| **ViewModel & LiveData** | `lifecycle-viewmodel`, `lifecycle-livedata`, `lifecycle-runtime` |
| **Coroutines** | `kotlinx.coroutines.android`, `kotlinx.coroutines.core` |
| **PDF Oluşturma** | `iText core` |
| **CSV Dışa Aktarım** | `Apache Commons CSV` |
| **Unit Test** | `junit`, `mockk`, `turbine`, `truth`, `core-testing`, `kotlinx-coroutines-test` |
| **Instrumented Test** | `androidx.test.ext.junit`, `androidx.test.espresso.core` |


## 📘 Commit Geçmişi

Projeye ait ilerlemeler aşağıdaki commit mesajları ile belgelenmiştir:

- `Edited app icon and splash screen
- `added NotificationService and code review`
- `added unit test`
- `Added extraction as pdf and csv`
- `added basefragment`
- `added user control, users are ensured to be unique`
- `added filter for product`
- `Added filter for suppliers`
- `Added splash screen and login screen animation`
- `Added transaction page`
- `Added filter product`
- `Added supplier`
- `Added transactionDb and dashboard review`
- `Added feature to edit products`
- `Added AddOrEditProduct logic`
- `Added ProductsFragment and review bottom navigation bar`
- `Added two adapters to DashboardFragment, mock data, and perform code review`
- `Added navigation graph, single activity multi fragment, mvvm, loginstate`
- `First Commit and Initial project setup with Room, Hilt, Coroutines, Flow, LiveData`

  
