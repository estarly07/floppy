import 'dart:convert';

import 'package:animate_do/animate_do.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:share/share.dart';
import 'package:sticker_floppy/Collections/model/collection.dart';
import 'package:sticker_floppy/widgets/widgets.dart';

class DetailCollection extends StatelessWidget {
  const DetailCollection({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    final collection = ModalRoute.of(context)!.settings.arguments as Collection;
    return Scaffold(
        body: Stack(
      children: [
        Container(
          width: double.infinity,
          height: size.height * 0.25,
          child: DecorationFondo(),
        ),
        Container(
          margin: EdgeInsets.symmetric(horizontal: size.width * 0.015),
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  height: size.width * 0.15,
                ),
                Container(
                    margin: EdgeInsets.symmetric(vertical: 10),
                    child: GestureDetector(
                      onTap: () {
                        Navigator.pop(context);
                      },
                      child: Icon(
                        Icons.arrow_back_ios_new_rounded,
                      ),
                    )),
                FadeInDown(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        collection.nameCollection,
                        maxLines: 2,
                        style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: size.height * 0.04),
                      ),
                      Container(
                        margin:
                            EdgeInsets.only(left: 0.2, top: size.height * 0.03),
                        child: GestureDetector(
                          onTap: () {
                            Share.share(collection.toMap().toString());
                          },
                          child: CustomButton(
                              text: "Compartir",
                              width: size.width * 0.35,
                              texColor: Colors.black,
                              background: Colors.white),
                        ),
                      )
                    ],
                  ),
                ),
                ElasticInDown(
                  child: Container(
                      padding: EdgeInsets.all(size.width * 0.05),
                      child: GridView(
                          shrinkWrap: true,
                          physics: NeverScrollableScrollPhysics(),
                          children: collection.stickers
                              .map((e) => GestureDetector(
                                  onTap: () {}, child: CardSticker(sticker: e)))
                              .toList(),
                          gridDelegate:
                              SliverGridDelegateWithMaxCrossAxisExtent(
                                  maxCrossAxisExtent: (size.width * 0.3),
                                  mainAxisExtent: (size.height * 0.15),
                                  crossAxisSpacing: 5,
                                  mainAxisSpacing: 0))),
                )
              ],
            ),
          ),
        ),
      ],
    ));
  }
}
