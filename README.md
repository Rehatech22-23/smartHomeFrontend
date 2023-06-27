<h1>Overview:</h1>
<p>
The smarthome frontend of the rehatech smarthome project was created in the minor "Rehabilitationstechnologie" from the major computer science at the TU-Dortmund in the summer semester of 2023 and is licenced under GPL-3.0.
The frontend repository inhabits the source files from the android app, which tries to create an accessible UI for interacting and controlling different smarthome components.
</p>
<h1>Technical specifications:</h1>
<ul>
  <li>Android studio (min. android sdk 26, compiled on 33)</li>
  <li>Kotlin (jdk version 17)</li>
  <li>Kotlinx-serialization</li>
  <li>OkHttp</li>
  <li>SmartHomeSharedLibrary</li>
</ul>
<h1>Installation Advice:</h1>
<ol>
  <li>Clone the repository and open it in Android Studio</li>
  <li>Connect a valid android device via usb and add it as a device</li>
  <li>Build and Run the project with the device selected, the app gets installed automatically</li>
</ol>
<h1>Adding custom colorschemes</h1>
<ol>
  <li>Add the colors you want to use in res/values/colors.xml</li>
  <li>
    Add a new style in res/values/themes.xml.
    Your custom colors can be used as the main and background colors, important fields and below should only use (potentially custom) drawables and the color-items defined above 
  </li>
  <li>
      Add your theme into ThemeEnum.kt
  </li>
  <li>
      Edit res/layout/option_settings.xml, add a new RadioButton and provide it with an unique Id
  </li>
  <li>
      In ui/options/OptionSettings.kt edit the following methods to include your new RadioButton and your color-theme:
    <ul>
      <li>init</li>
      <li>loadState</li>
      <li>changeColorTheme</li>
      <li>initColorSchemeButtons</li>
    </ul>
  </li>
  <li>The new color-theme should be available in the option menu</li>
</ol>
<br>
<h1>Überblick:</h1>
<p>
Das Smarthome-Frontend des rehatech-Smarthome-Projekts wurde im Nebenfach "Rehabilitationstechnologie" des Studiengangs Informatik an der TU-Dortmund im Sommersemester 2023 erstellt und steht unter der GPL-3.0-Lizenz. Das Frontend-Repository enthält die Quelldateien der Android-App, die versucht, eine zugängliche Benutzeroberfläche für die Interaktion und Steuerung verschiedener Smarthome-Komponenten zu schaffen.
</p>
<h1>Technische Spezifikationen:</h1>
<ul>
  <li>Android Studio (mind. android sdk 26, kompiliert auf 33)</li>
  <li>Kotlin (jdk Version 17)</li>
  <li>Kotlinx-serialization</li>
  <li>OkHttp</li>
  <li>SmartHomeSharedLibrary</li>
</ul>
<h1>Hinweise zur Installation:</h1>
<ol>
  <li>Klonen Sie das Repository und öffnen Sie es in Android Studio</li>
  <li>Schließen Sie ein gültiges Android-Gerät über USB an und fügen Sie es als Gerät hinzu</li>
  <li>Bauen Sie das Projekt und führen Sie es mit dem ausgewählten Gerät aus, die App wird automatisch installiert</li>
</ol>
<h1>Hinzufügen eigener Farbschemata</h1>
<ol>
  <li>Fügen Sie die Farben, die Sie verwenden möchten, in res/values/colors.xml hinzu</li>
  <li>
    Fügen Sie einen neuen Stil in res/values/themes.xml hinzu. Ihre benutzerdefinierten Farben können als Haupt- und Hintergrundfarben verwendet werden,       wichtige Felder und darunter sollten nur (potentiell benutzerdefinierte) Drawables und die oben definierten Color-Items verwenden
  </li>
  <li>
      Fügen Sie Ihr Thema in ThemeEnum.kt ein
  </li>
  <li>
      Bearbeiten Sie res/layout/option_settings.xml, fügen Sie einen neuen RadioButton hinzu und versehen Sie ihn mit einer eindeutigen Id
  </li>
  <li>
      Bearbeiten Sie in ui/options/OptionSettings.kt die folgenden Methoden, um Ihren neuen RadioButton und Ihr Farbthema einzubinden:
    <ul>
      <li>init</li>
      <li>loadState</li>
      <li>changeColorTheme</li>
      <li>initColorSchemeButtons</li>
    </ul>
  </li>
  <li>Das neue Farbthema sollte im Optionsmenü verfügbar sein</li>
</ol>
