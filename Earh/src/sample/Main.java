package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Main extends Application {
    //private static final float WIDTH = 800;
    //private static final float HEIGHT = 600;
    private static final float WIDTH = 1400;
    private static final float HEIGHT = 1000;
    private double anchorX,anchorY;
    private double anchorAngX=0;
    private double anchorAngY=0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final Sphere sphere = new Sphere(150);



    @Override
    public void start(Stage primaryStage) throws Exception{
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

        Group world = new Group();
        world.getChildren().add(preparEarh());

        Slider slider = prepareSlider();
        world.translateZProperty().bind(slider.valueProperty());

        Group root = new Group();
        root.getChildren().add(world);
        root.getChildren().add(prepareImageView());
        root.getChildren().add(slider);
        //root.getChildren().addAll(preParedLightSrc());

        Scene scene = new Scene(root,WIDTH, HEIGHT,true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        initMouseControl(world,scene,primaryStage);
        preparAnimation();

        primaryStage.setTitle("Naor3D");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void preparAnimation() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.rotateProperty().set(sphere.getRotate()+0.2);
            }
        };
        animationTimer.start();
    }

    private void initMouseControl(Group group, Scene scene,Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0,Rotate.X_AXIS),
                yRotate= new Rotate(0,Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngX=angleX.get();
            anchorAngY=angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngX-(anchorY-event.getSceneY()));
            angleY.set(anchorAngY+anchorX-event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ()+deltaY);
        });
    }

    private Node preparEarh() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/earth-dd.jpg")));
        material.setSpecularMap(new Image(getClass().getResourceAsStream("/resources/earth-ss.jpg")));
        material.setBumpMap(new Image(getClass().getResourceAsStream("/resources/earth-nn.jpg")));
        material.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/earth-lW.jpg")));
        //material.setSpecularColor(Color.WHITE);

        sphere.setMaterial(material);
        sphere.setRotationAxis(Rotate.Y_AXIS);

        return sphere;
    }

    private ImageView prepareImageView(){
        Image image = new Image(getClass().getResourceAsStream("/resources/galaxy.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth()/2,-image.getHeight()/2,800));
        return imageView;
    }

    private Slider prepareSlider(){
        Slider slider = new Slider();
        slider.setMax(400);
        slider.setMin(-100);
        slider.setPrefWidth(450d);
        slider.setLayoutX(-150);
        slider.setLayoutY(200);
        slider.setShowTickLabels(true);
        slider.setTranslateZ(5);
        slider.setStyle("-fx-base: black");
        return slider;
    }
    /*private Node[] preParedLightSrc() {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(Color.AQUA);

        pointLight.setColor(Color.RED);
        pointLight.getTransforms().add(new Translate(0,-50,100));
        pointLight.setRotationAxis(Rotate.X_AXIS);

        Sphere sphere = new Sphere(2);
        sphere.getTransforms().setAll(pointLight.getTransforms());
        sphere.rotateProperty().bind(pointLight.rotateProperty());
        sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());
        return  new Node[]{pointLight,sphere};

    }*/




    public static void main(String[] args) {
        launch(args);
    }
}
