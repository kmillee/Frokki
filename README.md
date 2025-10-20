# Pond_Simulator
forgs

## TODO:
  - [ ] add popup when catching a main.java.frog to ask for its name (implement random name thing)
  - [ ] set timer and maximum nb of things in the main.java.pond
  - [ ] add sounds (frogs, scissors...)
  - [ ] toolbox decorate frame (+ handle close and moving operation)
  - [ ] do pixel art assets


- [ ] main.java.frog.Frog bar
    - [x] display selected main.java.frog onto task bar
    - [x] Drag and drop to add main.java.frog to main.java.frog bar
    - [x] left click on main.java.frog, it jumps
    - [x] right click, show main.java.frog-related options
    - [ ] crocodile appear when a snake is in the main.java.pond

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
      - [ ] clean main.java.pond: 
  - [x] main.java.frog.Frog menu
      - [x] left panel: selected main.java.frog information
      - [x] right panel: list of all collected frogs
  - [ ] Quit
  - [ ] Tips

- [ ] main.java.pond.Pond:
    - [x] transparent window that displays a main.java.pond
    - [ ] frogs appear randomly on lilypads
    - [ ] lilypads appear randomly
    - [ ] reed appear randomly
    - [ ] lilypad can start to rot
    - [ ] duck appear rarely, when click on it, it appears in the task bar
    - [ ] crocodile appear rarely


- [x] Serializer/deserializer: json file to store locally player's data 


https://www.codecademy.com/article/mvc-architecture-model-view-controller
- Model: The Model is responsible for the data and business logic of the application. It represents the state of the application and handles the logic for updating that state:
    - main.java.frog.Frog.java: Represents the data structure for a main.java.frog (e.g., name, species, experience, image path).
    - PondSimModel.java: Manages the overall state of the main.java.pond simulation, such as the list of frogs, lilypads, reeds, and other entities in the main.java.pond.
    - Serializer.java: Handles saving and loading player data (e.g., frogs, main.java.pond state) to and from a JSON file.
- View: The View is responsible for the user interface and displaying data to the user. It listens to the controller for updates and renders the state of the model:
    - main.java.frog.FrogBar.java: Displays the main.java.frog bar on the taskbar, allowing interactions like clicking to make frogs jump.
    - PondSimView.java: Represents the main main.java.pond view, showing the main.java.pond, lilypads, frogs, reeds, and other visual elements.
    - main.java.frogedex.Frogedex.java: Displays the main.java.frogedex.Frogedex, showing the list of collected frogs and their details.
- Component: The Controller is responsible for handling user input and updating the model or view accordingly. It acts as the intermediary between the model and the view:
    - PondSimController.java: Handles user interactions (e.g., clicking on frogs, using tools, toggling settings) and updates the model or view as needed.