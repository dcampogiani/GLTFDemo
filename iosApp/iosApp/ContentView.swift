import SwiftUI
import shared

struct ContentView: View {
    
    @ObservedObject var model = ObservableUseCase()
    
    var body: some View {
        List(model.models) { item in
            Button(action : {
                UIApplication.shared.open(URL(string: "https://arvr.google.com/scene-viewer/1.0?file="+item.url)!)
            }){
                Text(item.name)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

final class ObservableUseCase : ObservableObject{
    
    init(){
        fetchData()
    }
    
    @Published var models = [Model]()
    
    private func fetchData(){
    let useCase = FetchModelsUseCase(api: ApiImplementation(),parser: ParserImplementation())
    
    useCase.invoke(){ result, error in
       if let e = error {
           print("ERROR: \(e)")
       } else {
        self.models = result!
       }
    }
    }

}
extension Model : Identifiable{
    public var id : String { name }
}
