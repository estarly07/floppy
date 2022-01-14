import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class SliderModel {
  Color color;
  final String image;
  final String title;
  final String description;

  SliderModel(
      {required this.image,
      required this.title,
      required this.description,
      this.color = Colors.white});
}
