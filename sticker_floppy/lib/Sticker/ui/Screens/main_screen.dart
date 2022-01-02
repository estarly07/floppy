import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sticker_floppy/Collections/bloc/collections_bloc.dart';
import 'package:sticker_floppy/Collections/model/collection.dart';
import 'package:sticker_floppy/Sticker/model/sticker.dart';
import 'package:sticker_floppy/widgets/widgets.dart';
import 'package:sticker_floppy/Utils/colors.dart';

class MainScreen extends StatelessWidget {
  MainScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    BlocProvider.of<CollectionsBloc>(context).add(GetCollectionsEvent());
    return Scaffold(
      body: BlocBuilder<CollectionsBloc, CollectionsState>(
        builder: (context, state) {
          return SafeArea(
            child: Stack(
              children: [
                Container(
                  width: double.infinity,
                  height: size.height * 0.25,
                  child: _Decoration(),
                ),
                (state.collections.isEmpty)
                    ? Center(
                        child: CircularProgressIndicator(
                        color: colors[1],
                      ))
                    : Container(
                        margin: EdgeInsets.all(10),
                        child: SingleChildScrollView(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              _Titles(),
                              _Collections(collections: state.collections),
                              Column(
                                children: state.collections
                                    .map((e) => _Stickers(
                                        stickers: e.stickers,
                                        nameCollection: e.nameCollection))
                                    .toList(),
                              )
                            ],
                          ),
                        ),
                      ),
              ],
            ),
          );
        },
      ),
    );
  }
}

class _Decoration extends StatelessWidget {
  _Decoration({
    Key? key,
  }) : super(key: key);

  var firstCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[2]]);
  var secondCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[1]]);
  var thirdCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[3]]);
  var fourthCircle = LinearGradient(
      begin: Alignment.topLeft,
      end: Alignment.bottomRight,
      stops: [0.01, 0.6],
      colors: [Colors.white, colors[0]]);
  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
      child: Stack(
        fit: StackFit.expand,
        children: [
          Positioned(
              top: size.height * -0.1,
              bottom: size.height * 0.05,
              right: size.height * -0.05,
              left: size.height * 0.25,
              child: Circle(width: size.width * 0.25, gradient: firstCircle)),
          Positioned(
              top: size.height * -0.2,
              bottom: size.height * 0.035,
              right: size.height * -0.1,
              left: size.height * 0.38,
              child: Circle(width: size.width * 0.25, gradient: secondCircle)),
          Positioned(
              top: size.height * 0.12,
              bottom: size.height * 0.03,
              right: size.height * 0.065,
              left: size.height * 0.2,
              child: Circle(width: size.width * 0.5, gradient: fourthCircle)),
          Positioned(
              top: size.height * 0.1,
              bottom: size.height * 0.11,
              right: size.height * 0.05,
              left: size.height * 0.2,
              child: Circle(width: size.width * 0.5, gradient: thirdCircle)),
        ],
      ),
    );
  }
}

class _Titles extends StatelessWidget {
  const _Titles({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
      margin: EdgeInsets.only(bottom: 25, top: size.height * 0.1),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            "Floppy",
            maxLines: 1,
            style: TextStyle(
                fontSize: 30,
                fontWeight: FontWeight.bold,
                color: Colors.grey[600]),
          ),
          Text(
            "Stickers",
            maxLines: 1,
            style: TextStyle(
                fontSize: 50,
                fontWeight: FontWeight.bold,
                color: Colors.blue[600]),
          ),
        ],
      ),
    );
  }
}

class _Collections extends StatelessWidget {
  final List<Collection> collections;
  const _Collections({
    Key? key,
    required this.collections,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          "Colecciones",
          maxLines: 1,
          style: TextStyle(
              fontSize: size.height * 0.025,
              fontWeight: FontWeight.bold,
              color: Colors.grey[900]),
        ),
        Container(
            height: size.height * 0.32,
            margin: EdgeInsets.symmetric(vertical: size.height * 0.02),
            width: double.infinity,
            child: ListView.builder(
              physics: BouncingScrollPhysics(),
              scrollDirection: Axis.horizontal,
              itemBuilder: (_, i) {
                return CardCollection(
                  collection: collections[i],
                  color: colors[Random().nextInt(colors.length)],
                );
              },
              itemCount: collections.length,
            )),
      ],
    );
  }
}

class _Stickers extends StatelessWidget {
  final List<Sticker> stickers;
  final String nameCollection;
  const _Stickers({
    Key? key,
    required this.stickers,
    required this.nameCollection,
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
            nameCollection,
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
              itemBuilder: (_, i) => CardSticker(
                sticker: stickers[i],
              ),
              itemCount: stickers.length,
            ),
          ),
        ],
      ),
    );
  }
}
