package org.block.project.block;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.transform.Transform;
import javafx.util.Callback;

import java.util.Set;

public interface BlockNode<B extends Block> extends Styleable, EventTarget {

    B getBlock();

    void updateBlock();

    double getWidth();

    double getHeight();

    ObjectProperty<String> accessibleHelpProperty();

    ObjectProperty<String> accessibleRoleDescriptionProperty();

    ObjectProperty<AccessibleRole> accessibleRoleProperty();

    ObjectProperty<String> accessibleTextProperty();

    ObjectProperty<BlendMode> blendModeProperty();

    ObjectProperty<CacheHint> cacheHintProperty();

    BooleanProperty cacheProperty();

    ObjectProperty<Node> clipProperty();

    ObjectProperty<Cursor> cursorProperty();

    ObjectProperty<DepthTest> depthTestProperty();

    BooleanProperty disableProperty();

    ObjectProperty<Effect> effectProperty();

    ObjectProperty<EventDispatcher> eventDispatcherProperty();

    BooleanProperty focusTraversableProperty();

    StringProperty idProperty();

    ObjectProperty<InputMethodRequests> inputMethodRequestsProperty();

    DoubleProperty layoutXProperty();

    DoubleProperty layoutYProperty();

    BooleanProperty managedProperty();

    BooleanProperty mouseTransparentProperty();

    ObjectProperty<NodeOrientation> nodeOrientationProperty();

    ReadOnlyObjectProperty<Bounds> boundsInLocalProperty();

    ReadOnlyObjectProperty<Bounds> boundsInParentProperty();

    ReadOnlyBooleanProperty disabledProperty();

    ReadOnlyObjectProperty<NodeOrientation> effectiveNodeOrientationProperty();

    ReadOnlyBooleanProperty focusedProperty();

    ReadOnlyBooleanProperty hoverProperty();

    ReadOnlyObjectProperty<Bounds> layoutBoundsProperty();

    ReadOnlyObjectProperty<Transform> localToParentTransformProperty();

    ReadOnlyObjectProperty<Transform> localToSceneTransformProperty();

    <T extends Event>

    void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventHandler);

    <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);

    EventHandler<? super ContextMenuEvent> getOnContextMenuRequested();

    EventHandler<? super MouseEvent> getOnDragDetected();

    EventHandler<? super DragEvent> getOnDragDone();

    EventHandler<? super DragEvent> getOnDragDropped();

    EventHandler<? super DragEvent> getOnDragEntered();

    EventHandler<? super DragEvent> getOnDragExited();

    EventHandler<? super DragEvent> getOnDragOver();

    EventHandler<? super InputMethodEvent> getOnInputMethodTextChanged();

    EventHandler<? super KeyEvent> getOnKeyPressed();

    EventHandler<? super KeyEvent> getOnKeyReleased();

    EventHandler<? super KeyEvent> getOnKeyTyped();

    EventHandler<? super MouseEvent> getOnMouseClicked();

    EventHandler<? super MouseDragEvent> getOnMouseDragEntered();

    EventHandler<? super MouseDragEvent> getOnMouseDragExited();

    EventHandler<? super MouseEvent> getOnMouseDragged();

    EventHandler<? super MouseDragEvent> getOnMouseDragOver();

    EventHandler<? super MouseDragEvent> getOnMouseDragReleased();

    EventHandler<? super MouseEvent> getOnMouseEntered();

    EventHandler<? super MouseEvent> getOnMouseExited();

    EventHandler<? super MouseEvent> getOnMouseMoved();

    EventHandler<? super MouseEvent> getOnMousePressed();

    EventHandler<? super MouseEvent> getOnMouseReleased();

    EventHandler<? super RotateEvent> getOnRotate();

    EventHandler<? super RotateEvent> getOnRotationFinished();

    EventHandler<? super RotateEvent> getOnRotationStarted();

    EventHandler<? super ScrollEvent> getOnScroll();

    EventHandler<? super ScrollEvent> getOnScrollFinished();

    EventHandler<? super ScrollEvent> getOnScrollStarted();

    EventHandler<? super SwipeEvent> getOnSwipeDown();

    EventHandler<? super SwipeEvent> getOnSwipeLeft();

    EventHandler<? super SwipeEvent> getOnSwipeRight();

    EventHandler<? super SwipeEvent> getOnSwipeUp();

    EventHandler<? super TouchEvent> getOnTouchMoved();

    EventHandler<? super TouchEvent> getOnTouchPressed();

    EventHandler<? super TouchEvent> getOnTouchReleased();

    EventHandler<? super TouchEvent> getOnTouchStationary();

    EventHandler<? super ZoomEvent> getOnZoom();

    EventHandler<? super ZoomEvent> getOnZoomFinished();

    EventHandler<? super ZoomEvent> getOnZoomStarted();

    double computeAreaInScreen();

    String getAccessibleHelp();

    AccessibleRole getAccessibleRole();

    String getAccessibleRoleDescription();

    String getAccessibleText();

    double getBaselineOffset();

    BlendMode getBlendMode();

    CacheHint getCacheHint();

    Node getClip();

    Orientation getContentBias();

    Cursor getCursor();

    DepthTest getDepthTest();

    Effect getEffect();

    NodeOrientation getEffectiveNodeOrientation();

    EventDispatcher getEventDispatcher();

    InputMethodRequests getInputMethodRequests();

    double getLayoutX();

    double getLayoutY();

    Transform getLocalToParentTransform();

    Transform getLocalToSceneTransform();

    NodeOrientation getNodeOrientation();

    double getOpacity();

    Parent getParent();

    ObservableMap<Object, Object> getProperties();

    double getRotate();

    Point3D getRotationAxis();

    double getScaleX();

    double getScaleY();

    double getScaleZ();

    Scene getScene();

    ObservableList<Transform> getTransforms();

    double getTranslateX();

    double getTranslateY();

    double getTranslateZ();

    Object getUserData();

    double getViewOrder();

    boolean hasProperties();

    boolean intersects(double localX, double localY, double localWidth, double localHeight);

    boolean intersects(Bounds localBounds);

    boolean isCache();

    boolean isDisable();

    boolean isDisabled();

    boolean isFocused();

    boolean isFocusTraversable();

    boolean isHover();

    boolean isManaged();

    boolean isMouseTransparent();

    boolean isPickOnBounds();

    boolean isPressed();

    boolean isResizable();

    boolean isVisible();

    Point2D localToParent(double localX, double localY);

    Point3D localToParent(double localX, double localY, double localZ);

    Bounds localToParent(Bounds localBounds);

    Point2D localToParent(Point2D localPoint);

    Point3D localToParent(Point3D localPoint);

    Point2D localToScene(double localX, double localY);

    Point3D localToScene(double localX, double localY, double localZ, boolean rootScene);

    Bounds localToScene(Bounds localBounds);

    Bounds localToScene(Bounds localBounds, boolean rootScene);

    Point2D localToScene(Point2D localPoint);

    Point2D localToScene(Point2D localPoint, boolean rootScene);

    Point3D localToScene(Point3D localPoint);

    Point3D localToScene(Point3D localPoint, boolean rootScene);

    Point2D localToScreen(double localX, double localY);

    Point2D localToScreen(double localX, double localY, double localZ);

    Bounds localToScreen(Bounds localBounds);

    Point2D localToScreen(Point2D localPoint);

    Point2D localToScreen(Point3D localPoint);

    Node lookup(String selector);

    Set<Node> lookupAll(String selector);

    double maxHeight(double height);

    double maxWidth(double width);

    double minHeight(double height);

    double minWidth(double width);

    void notifyAccessibleAttributeChanged(AccessibleAttribute attributes);

    ObjectProperty<EventHandler<? super ContextMenuEvent>> onContextMenuRequestedProperty();

    ObjectProperty<EventHandler<? super MouseEvent>> onDragDetectedProperty();

    ObjectProperty<EventHandler<? super DragEvent>> onDragDoneProperty();

    ObjectProperty<EventHandler<? super DragEvent>> onDragDroppedProperty();

    ObjectProperty<EventHandler<? super DragEvent>> onDragEnteredProperty();

    ObjectProperty<EventHandler<? super DragEvent>> onDragExitedProperty();

    ObjectProperty<EventHandler<? super DragEvent>> onDragOverProperty();

    ObjectProperty<EventHandler<? super InputMethodEvent>> onInputMethodTextChangedProperty();

    ObjectProperty<EventHandler<? super KeyEvent>> onKeyPressedProperty();

    ObjectProperty<EventHandler<? super KeyEvent>> onKeyReleasedProperty();

    ObjectProperty<EventHandler<? super KeyEvent>> onKeyTypedProperty();

    ObjectProperty<EventHandler<? super MouseEvent>> onMouseClickedProperty();

    ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragEnteredProperty();

    ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragExitedProperty();

    ObjectProperty<EventHandler<? super MouseEvent>> onMouseDraggedProperty();

    ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragOverProperty();

    ObjectProperty<EventHandler<? super MouseDragEvent>> onMouseDragReleasedProperty();

    ObjectProperty<EventHandler<? super MouseEvent>> onMouseEnteredProperty();

    ObjectProperty<EventHandler<? super MouseEvent>> onMouseExitedProperty();

    ObjectProperty<EventHandler<? super MouseEvent>> onMouseMovedProperty();

    ObjectProperty<EventHandler<? super MouseEvent>> onMousePressedProperty();


    ObjectProperty<EventHandler<? super RotateEvent>> onRotateProperty();

    ObjectProperty<EventHandler<? super RotateEvent>> onRotationFinishedProperty();

    ObjectProperty<EventHandler<? super RotateEvent>> onRotationStartedProperty();

    ObjectProperty<EventHandler<? super ScrollEvent>> onScrollFinishedProperty();

    ObjectProperty<EventHandler<? super ScrollEvent>> onScrollProperty();

    ObjectProperty<EventHandler<? super ScrollEvent>> onScrollStartedProperty();

    ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeDownProperty();

    ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeLeftProperty();

    ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeRightProperty();

    ObjectProperty<EventHandler<? super SwipeEvent>> onSwipeUpProperty();

    ObjectProperty<EventHandler<? super TouchEvent>> onTouchMovedProperty();

    ObjectProperty<EventHandler<? super TouchEvent>> onTouchPressedProperty();

    ObjectProperty<EventHandler<? super TouchEvent>> onTouchReleasedProperty();

    ObjectProperty<EventHandler<? super TouchEvent>> onTouchStationaryProperty();

    ObjectProperty<EventHandler<? super ZoomEvent>> onZoomFinishedProperty();

    ObjectProperty<EventHandler<? super ZoomEvent>> onZoomProperty();

    DoubleProperty opacityProperty();

    ReadOnlyObjectProperty<Parent> parentProperty();

    Point2D parentToLocal(double parentX, double parentY);

    Point3D parentToLocal(double parentX, double parentY, double parentZ);

    Bounds parentToLocal(Bounds parentBounds);

    Point2D parentToLocal(Point2D parentPoint);

    Point3D parentToLocal(Point3D parentPoint);

    BooleanProperty pickOnBoundsProperty();

    double prefHeight(double height);

    double prefWidth(double width);

    ReadOnlyBooleanProperty pressedProperty();

    void pseudoClassStateChanged(PseudoClass pseudoClass, boolean active);

    Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters);

    void relocate(double x, double y);

    <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter);

    <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);

    void requestFocus();

    void resize(double width, double height);

    void resizeRelocate(double x, double y, double width, double height);

    DoubleProperty rotateProperty();

    ObjectProperty<Point3D> rotationAxisProperty();

    DoubleProperty scaleXProperty();

    DoubleProperty scaleYProperty();

    DoubleProperty scaleZProperty();

    ReadOnlyObjectProperty<Scene> sceneProperty();

    Point2D sceneToLocal(double sceneX, double sceneY);

    Point2D sceneToLocal(double sceneX, double sceneY, boolean rootScene);

    Point3D sceneToLocal(double sceneX, double sceneY, double sceneZ);

    Bounds sceneToLocal(Bounds sceneBounds);

    Bounds sceneToLocal(Bounds bounds, boolean rootScene);

    Point2D sceneToLocal(Point2D scenePoint);

    Point2D sceneToLocal(Point2D scenePoint, boolean rootScene);

    Point3D sceneToLocal(Point3D scenePoint);

    Point2D screenToLocal(double screenX, double screenY);

    Bounds screenToLocal(Bounds screenBounds);

    Point2D screenToLocal(Point2D screenPoint);

    void setAccessibleHelp(String value);

    void setAccessibleRole(AccessibleRole value);

    void setAccessibleRoleDescription(String value);

    void setAccessibleText(String value);

    void setBlendMode(BlendMode value);

    void setCache(boolean value);

    void setCacheHint(CacheHint value);

    void setClip(Node value);

    void setCursor(Cursor value);

    void setDepthTest(DepthTest value);

    void setDisable(boolean value);

    void setEffect(Effect value);

    void setEventDispatcher(EventDispatcher value);

    void setFocusTraversable(boolean value);

    void setId(String value);

    void setInputMethodRequests(InputMethodRequests value);

    void setLayoutX(double x);

    void setLayoutY(double y);

    void setManaged(boolean value);

    void setMouseTransparent(boolean value);

    void setNodeOrientation(NodeOrientation orientation);

    void setOnContextMenuRequested(EventHandler<? super ContextMenuEvent> value);

    void setOnDragDetected(EventHandler<? super MouseEvent> value);

    void setOnDragDropped(EventHandler<? super DragEvent> value);

    void setOnDragEntered(EventHandler<? super DragEvent> value);

    void setOnDragExited(EventHandler<? super DragEvent> value);

    void setOnDragOver(EventHandler<? super DragEvent> value);

    void setOnInputMethodTextChanged(EventHandler<? super InputMethodEvent> value);

    void setOnKeyPressed(EventHandler<? super KeyEvent> value);

    void setOnKeyReleased(EventHandler<? super KeyEvent> value);

    void setOnKeyTyped(EventHandler<? super KeyEvent> value);

    void setOnMouseClicked(EventHandler<? super MouseEvent> value);

    void setOnMouseDragEntered(EventHandler<? super MouseDragEvent> value);

    void setOnMouseDragExited(EventHandler<? super MouseDragEvent> value);

    void setOnMouseDragged(EventHandler<? super MouseEvent> value);

    void setOnMouseDragOver(EventHandler<? super MouseDragEvent> value);

    void setOnMouseDragReleased(EventHandler<? super MouseDragEvent> value);

    void setOnMouseEntered(EventHandler<? super MouseEvent> value);

    void setOnMouseExited(EventHandler<? super MouseEvent> value);

    void setOnMouseMoved(EventHandler<? super MouseEvent> value);

    void setOnMousePressed(EventHandler<? super MouseEvent> value);

    void setOnMouseReleased(EventHandler<? super MouseEvent> value);

    void setOnRotate(EventHandler<? super RotateEvent> value);

    void setOnRotationFinished(EventHandler<? super RotateEvent> value);

    void setOnRotationStarted(EventHandler<? super RotateEvent> value);

    void setOnScroll(EventHandler<? super ScrollEvent> value);

    void setOnScrollFinished(EventHandler<? super ScrollEvent> value);

    void setOnScrollStarted(EventHandler<? super ScrollEvent> value);

    void setOnSwipeDown(EventHandler<? super SwipeEvent> value);

    void setOnSwipeLeft(EventHandler<? super SwipeEvent> value);

    void setOnSwipeRight(EventHandler<? super SwipeEvent> value);

    void setOnSwipeUp(EventHandler<? super SwipeEvent> value);

    void setOnTouchMoved(EventHandler<? super TouchEvent> value);

    void setOnTouchPressed(EventHandler<? super TouchEvent> value);

    void setOnTouchReleased(EventHandler<? super TouchEvent> value);

    void setOnTouchStationary(EventHandler<? super TouchEvent> value);

    void setOnZoom(EventHandler<? super ZoomEvent> value);

    void setOnZoomFinished(EventHandler<? super ZoomEvent> value);

    void setOnZoomStarted(EventHandler<? super ZoomEvent> value);

    void setOpacity(double value);

    void setPickOnBounds(boolean value);

    void setRotate(double value);

    void setRotationAxis(Point3D value);

    void setScaleX(double value);

    void setScaleY(double value);

    void setScaleZ(double value);

    void setStyle(String value);

    void setTranslateX(double value);

    void setTranslateY(double value);

    void setTranslateZ(double value);

    void setUserData(Object value);

    void setViewOrder(double value);

    void setVisible(boolean value);

    WritableImage snapshot(SnapshotParameters params, WritableImage image);

    void snapshot(Callback<SnapshotResult, Void> callable, SnapshotParameters params, WritableImage image);

    Dragboard startDragAndDrop(TransferMode... transferModes);

    void startFullDrag();

    StringProperty styleProperty();

    void toBack();

    void toFront();

    DoubleProperty translateXProperty();

    DoubleProperty translateYProperty();

    DoubleProperty translateZProperty();

    boolean usesMirroring();

    DoubleProperty viewOrderProperty();

    BooleanProperty visibleProperty();

    Bounds getBoundsInLocal();

    Bounds getBoundsInParent();

    Bounds getLayoutBounds();

    boolean contains(double localX, double localY);

    boolean contains(Point2D localPoint);

    void applyCss();

    void autosize();

    void executeAccessibleAction(AccessibleAction action, Object... parameters);

    void fireEvent(Event event);


}
