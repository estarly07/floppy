part of 'slider_bloc.dart';

@immutable
abstract class SliderState {
  final double currentPage;

  SliderState(this.currentPage);
}

class SliderInitial extends SliderState {
  SliderInitial() : super(0.0);
}

class ChangePageState extends SliderState {
  final double currentPage;

  ChangePageState(this.currentPage) : super(currentPage);
}
