import 'package:shared_preferences/shared_preferences.dart';

class Preferences {
  List<String> keys = ["firstTime"];
  static final Preferences _instance = Preferences._();
  Preferences._();

  factory Preferences() => _instance;
  late SharedPreferences _preferences;

  initPreferences() async {
    _preferences = await SharedPreferences.getInstance();
  }

  void firstTime() {
    _preferences.setBool(keys[0], !ifFirstTime);
  }

  bool get ifFirstTime => _preferences.getBool(keys[0]) ?? true;
}
