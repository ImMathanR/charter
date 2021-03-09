# Charter

A simple custom view demonstrating drawing line chart in canvas and animating it.

### Showcase
![Bezier curves path drawing animation](showcase/bezier_animation.gif "Bezier curves path drawing animation")

### Progress
1. Drawing axis lines
![Step 1](showcase/1.jpg "Step 1")
2. Plotting data points
![Step 2](showcase/2.jpg "Step 2")
3. Drawing lines along the data points
![Step 3](showcase/3.jpg "Step 3")
4. Drawing bezier curve along the data points
![Step 4](showcase/4.jpg "Step 4")


### Learning
* [Bezier curves](https://en.wikipedia.org/wiki/Bézier_curve) for drawing curved line paths
* [`PathMeasure.getSegment`](https://developer.android.com/reference/android/graphics/PathMeasure.html#getSegment(float%2C%20float%2C%20android.graphics.Path%2C%20boolean)) to copy a portion of a `Path` to a destination `Path` object

### Reference articles
* https://proandroiddev.com/drawing-bezier-curve-like-in-google-material-rally-e2b38053038c
* https://medium.com/androiddevelopers/playing-with-paths-3fbc679a6f77