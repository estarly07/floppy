import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:sticker_floppy/Sticker/ui/Widgets/widgets.dart';
import 'package:sticker_floppy/Utils/colors.dart';

class MainScreen extends StatelessWidget {
  const MainScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Container(
          margin: EdgeInsets.all(10),
          child: SingleChildScrollView(
            child: Column(
              children: [
                _Collections(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
                _Stickers(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _Collections extends StatelessWidget {
  const _Collections({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
        height: size.height * 0.35,
        margin: EdgeInsets.symmetric(vertical: size.height * 0.02),
        width: double.infinity,
        child: ListView.builder(
          physics: BouncingScrollPhysics(),
          scrollDirection: Axis.horizontal,
          itemBuilder: (_, i) {
            return CardCollection(
              color: colors[Random().nextInt(colors.length)],
            );
          },
          itemCount: 5,
        ));
  }
}

class _Stickers extends StatelessWidget {
  const _Stickers({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: size.width * 0.02),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            "Ms Been",
            maxLines: 1,
            style: TextStyle(
                fontSize: size.height * 0.025,
                fontWeight: FontWeight.bold,
                color: Colors.grey[900]),
          ),
          Container(
            width: double.infinity,
            height: size.height * 0.15,
            child: ListView.builder(
              physics: BouncingScrollPhysics(),
              scrollDirection: Axis.horizontal,
              itemBuilder: (_, i) => CardSticker(),
              itemCount: 10,
            ),
          ),
        ],
      ),
    );
  }
}
