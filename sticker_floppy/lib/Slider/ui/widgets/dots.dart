import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sticker_floppy/Slider/bloc/slider_bloc.dart';

class Dots extends StatelessWidget {
  int lenght;
  Dots(this.lenght);

  @override
  Widget build(BuildContext context) {
    final provider = BlocProvider.of<SliderBloc>(context, listen: false);
    final size = MediaQuery.of(context).size;
    return Container(
      width: double.infinity,
      child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: List.generate(
            lenght,
            (index) => _Ball(index),
          )),
    );
  }
}

class _Ball extends StatelessWidget {
  int index;
  _Ball(this.index);

  @override
  Widget build(BuildContext context) {
    final provider = BlocProvider.of<SliderBloc>(context, listen: false);
    final size = MediaQuery.of(context).size;

    return AnimatedContainer(
      duration: Duration(milliseconds: 200),
      margin: EdgeInsets.symmetric(horizontal: 5),
      height: (provider.state.currentPage.toInt() == index)
          ? size.width * 0.04
          : size.width * 0.03,
      width: (provider.state.currentPage.toInt() == index)
          ? size.width * 0.04
          : size.width * 0.03,
      decoration: BoxDecoration(
          color: (provider.state.currentPage.toInt() == index)
              ? Colors.white
              : Colors.grey,
          shape: BoxShape.circle),
    );
  }
}
