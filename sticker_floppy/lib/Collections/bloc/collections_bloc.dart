import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';
import 'package:sticker_floppy/Collections/data/collection_services.dart';
import 'package:sticker_floppy/Collections/model/collection.dart';

part 'collections_event.dart';
part 'collections_state.dart';

class CollectionsBloc extends Bloc<CollectionsEvent, CollectionsState> {
  final collectionService = CollectionService();

  CollectionsBloc() : super(CollectionsInitial()) {
    on<GetCollectionsEvent>((event, emit) async {
      emit(GetCollectionsState(await collectionService.getAllCollections()));
    });
  }
}
