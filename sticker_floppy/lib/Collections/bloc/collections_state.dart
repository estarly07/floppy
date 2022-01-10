part of 'collections_bloc.dart';

@immutable
abstract class CollectionsState {
  final List<Collection> collections;

  CollectionsState(this.collections);
}

class CollectionsInitial extends CollectionsState {
  CollectionsInitial() : super([]);
}

class GetCollectionsState extends CollectionsState {
  GetCollectionsState(List<Collection> collections) : super(collections);
}
