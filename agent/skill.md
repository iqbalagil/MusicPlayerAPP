# Java Swing GUI — Separation Rule (Netbeans)

## Core rule
All GUI code must be fully separated from business logic.
- GUI forms live in src/ui/ as .java + .form pairs
- Business logic lives in src/controller/ or src/service/
- Forms must never contain SQL, file I/O, or domain logic

## Netbeans .form convention
- Every screen has MyScreen.java + MyScreen.form
- Never hand-edit the initComponents() generated block
- Custom logic goes below the editor-fold or in separate classes

## Folder structure
src/
  ui/          .java + .form pairs (Netbeans GUI forms)
  controller/  Business logic triggered by GUI events
  model/       Data models / POJOs
  service/     DB, file I/O, APIs

## Not allowed in form classes
- SQL queries or JDBC calls
- File reading/writing
- Complex business calculations
- Modifying initComponents()

## Allowed in form classes
- Calling a controller method on button/event
- Displaying data returned by a controller
- Basic input validation (empty, format)
- Navigation between forms

## Pattern
// Form:
btnSave.addActionListener(e -> {
    String name = txtName.getText();
    ProductController.save(name); // delegate
});

// Controller:
public static void save(String name) {
    ProductService.insert(name);
}