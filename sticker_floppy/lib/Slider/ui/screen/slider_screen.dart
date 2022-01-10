import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sticker_floppy/Slider/ui/widgets/slider_item.dart';

class SliderScreen extends StatelessWidget {
  const SliderScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SliderItem(),
    );
  }
}
