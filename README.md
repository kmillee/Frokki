# Pond_Simulator
forgs

## TODO:
  - [ ] add popup when catching a frog to ask for its name (implement random name thing)
  - [X] set timer and maximum nb of things in the pond
  - [ ] add sounds (frogs, scissors...)
  - [ ] toolbox decorate frame (+ handle close and moving operation)
  - [ ] do pixel art assets


- [ ] frog.Frog bar
    - [x] display selected frog onto task bar
    - [x] Drag and drop to add frog to frog bar
    - [x] left click on frog, it jumps
    - [x] right click, show frog-related options
    - [ ] crocodile appear when a snake is in the pond

- [ ] Menu: 
  - [ ] icon in system tray, when click on it opens option/the menu
  - [ ] Tools menu
      - [x] net -> catch frogs
      - [ ] shears -> cut of reeds
      - [ ] bait -> place on lilypad to attract frogs
      - [ ] hand -> remove rotten lilypads
      - [ ] bell -> scare off crocodile
      - [ ] bin -> appears when hand is selected, to collect lilypads
  - [ ] Settings menu
      - [ ] Volume
      - [ ] snake on/off
      - [ ] clean pond: 
  - [x] frog.Frog menu
      - [x] left panel: selected frog information
      - [x] right panel: list of all collected frogs
  - [ ] Quit
  - [ ] Tips

- [ ] pond.Pond:
    - [x] transparent window that displays a pond
    - [ ] frogs appear randomly on lilypads
    - [ ] lilypads appear randomly
    - [ ] reed appear randomly
    - [ ] lilypad can start to rot
    - [ ] duck appear rarely, when click on it, it appears in the task bar
    - [ ] crocodile appear rarely


- [x] Serializer/deserializer: json file to store locally player's data 


https://www.codecademy.com/article/mvc-architecture-model-view-controller
- Model: The Model is responsible for the data and business logic of the application. It represents the state of the application and handles the logic for updating that state:
    - frog.Frog.java: Represents the data structure for a frog (e.g., name, species, experience, image path).
    - PondSimModel.java: Manages the overall state of the pond simulation, such as the list of frogs, lilypads, reeds, and other entities in the pond.
    - Serializer.java: Handles saving and loading player data (e.g., frogs, pond state) to and from a JSON file.
- View: The View is responsible for the user interface and displaying data to the user. It listens to the controller for updates and renders the state of the model:
    - frog.FrogBar.java: Displays the frog bar on the taskbar, allowing interactions like clicking to make frogs jump.
    - PondSimView.java: Represents the main pond view, showing the pond, lilypads, frogs, reeds, and other visual elements.
    - frogedex.Frogedex.java: Displays the frogedex.Frogedex, showing the list of collected frogs and their details.
- Component: The Controller is responsible for handling user input and updating the model or view accordingly. It acts as the intermediary between the model and the view:
    - PondSimController.java: Handles user interactions (e.g., clicking on frogs, using tools, toggling settings) and updates the model or view as needed.


When first launching the app, you will see a menu appear. From this menu you will be able to display the pond which is the place to collect frogs. 
Frogedex is the place to go whenever you want to see all the frogs you collected. If you need more explanation about how to collect frogs and how to interact
with them, keep on reading!

## Pond
Whenever this window is opened, elements will randomly appear in the pond. Reeds, lily pads, frogs or even crocodile can appear.
You will have to take care of that pond if you want frogs to actually appear, which they will only do if there is an available lily pad.
Beware, lily pads may start to rot which will impede frogs from spawning on it. You will have to use the hand tool to drag-and-drop the 
rotten lily pad into the bin. 
Also, a limited number of items can appear in the pond so you should cut off reeds using the scissors to allow for lily pads to spawn and
eventually frogs. 
When the crocodile spawns, all the frog go away. You have to scare the crocodile off using the bell
tool before any frog will come back to the pond.

## Frogedex
In the frogedex, you are able to see all the frogs you collected. You can see there a list
of all frogs you collected along with more detailed explanation about the currently selected 
frog in the list. 
If you want to put frogs onto your taskbar to act as your companions, you can do so
by either clicking on the summon button or dragging the frog from the detailed view to the taskbar.
You can interact with this frog by either left-clicking on it which will make it jump, or dragging and throwing
it. If you right-click on the frog, you will see a small card appear on top of it 
with some information regarding this specific frog.

That's all there is to know! 