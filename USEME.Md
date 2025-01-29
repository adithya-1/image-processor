# Image Processor User Manual

# Image Processor Interactive User Manual
## Table of Contents
- [Overview](#overview)
- [Basic Operations](#basic-operations)
- [Image Components](#image-components)
- [Image Transformations](#image-transformations)
- [Color Operations](#color-operations)
- [Effects and Filters](#effects-and-filters)
- [Analysis and Adjustment](#analysis-and-adjustment)
- [Program Control](#program-control)

## Overview
This document provides detailed instructions for using the Image Processor application. Each command follows a specific syntax and produces defined outputs.

## Basic Operations

### Loading and Saving Images
```
load image-path image-name
```
- **Purpose**: Load an image from the specified path
- **Parameters**:
    - `image-path`: Path to the source image file
    - `image-name`: Name to reference the image in the program
- **Example**: `load ./images/beach.jpg beach1`

```
save image-path image-name
```
- **Purpose**: Save an image to the specified path
- **Parameters**:
    - `image-path`: Destination path including filename
    - `image-name`: Name of the image to save
- **Example**: `save ./output/beach-modified.jpg beach1`

## Image Components

### Color Components
```
red-component image-name dest-image-name 
green-component image-name dest-image-name
blue-component image-name dest-image-name 
```
- **Purpose**: Extract individual RGB color components
- **Parameters**:
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
- **Example**: `red-component beach1 beach1-red`

### Value Components
```
value-component image-name dest-image-name 
luma-component image-name dest-image-name split [p]
intensity-component image-name dest-image-name
```
- **Purpose**: Create different representations of image brightness
- **Parameters**:
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
    - p: Optional parameter for split view
- **Example**: `luma-component beach1 beach1-luma`

## Image Transformations

### Flipping
```
horizontal-flip image-name dest-image-name
vertical-flip image-name dest-image-name
```
- **Purpose**: Flip image horizontally or vertically
- **Parameters**:
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
- **Example**: `horizontal-flip beach1 beach1-flipped`

### Brightness Adjustment
```
brighten increment image-name dest-image-name
```
- **Purpose**: Adjust image brightness
- **Parameters**:
    - `increment`: Positive (brighten) or negative (darken) value
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
- **Example**: `brighten 50 beach1 beach1-bright`

## Color Operations

### RGB Operations
```
rgb-split image-name dest-image-name-red dest-image-name-green dest-image-name-blue
```
- **Purpose**: Split image into RGB components
- **Parameters**:
    - `image-name`: Source image name
    - `dest-image-name-red/green/blue`: Names for component images
- **Example**: `rgb-split beach1 beach1-r beach1-g beach1-b`

```
rgb-combine image-name red-image green-image blue-image
```
- **Purpose**: Combine RGB components into single image
- **Parameters**:
    - `image-name`: Name for resulting image
    - `red/green/blue-image`: Names of component images
- **Example**: `rgb-combine beach1-new beach1-r beach1-g beach1-b`

## Effects and Filters

### Image Effects
```
blur image-name dest-image-name split [p]
sharpen image-name dest-image-name split [p]
sepia image-name dest-image-name split [p]
```
- **Purpose**: Apply various image effects
- **Parameters**:
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
    - `p`: Optional parameter for split view
- **Example**: `blur beach1 beach1-blurred`

### Compression
```
compress percentage image-name dest-image-name
```
- **Purpose**: Compress image by specified percentage
- **Parameters**:
    - `percentage`: Compression percentage (0-100)
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
- **Example**: `compress 50 beach1 beach1-compressed`

## Analysis and Adjustment

### Image Analysis
```
histogram image-name dest-image-name
```
- **Purpose**: Generate image histogram
- **Parameters**:
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting histogram image
- **Example**: `histogram beach1 beach1-hist`

### Color Adjustments
```
color-correct image-name dest-image-name split [p]
```
- **Purpose**: Perform automatic color correction
- **Parameters**:
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
    - `p`: Optional parameter for split view
- **Example**: `color-correct beach1 beach1-corrected`

```
levels-adjust b m w image-name dest-image-name split [p]
```
- **Purpose**: Adjust image levels
- **Parameters**:
    - `b`: Black point value
    - `m`: Mid point value
    - `w`: White point value
    - `image-name`: Source image name
    - `dest-image-name`: Name for the resulting image
    - `p`: Optional parameter for split view
- **Example**: `levels-adjust 20 128 255 beach1 beach1-adjusted`

## Program Control

### Script Execution
```
run script-file
```
- **Purpose**: Execute commands from a script file
- **Parameters**:
    - `script-file`: Path to script file
- **Example**: `run ./scripts/process-beach.txt`

### Program Commands
```
quit
```
- **Purpose**: Exit the program

```
reset
```
- **Purpose**: Reset the program memory and removes all in memory images.

```
help
```
- **Purpose**: Display help message with command information

# Image Processor GUI User Manual

## Table of Contents
- [Getting Started](#getting-started)
- [Basic Operations](#basic-operations)
- [Image Components](#image-components)
- [Image Transformations](#image-transformations)
- [Effects and Filters](#effects-and-filters)
- [Color Adjustments](#color-adjustments)
- [Advanced Features](#advanced-features)

## Getting Started

### Loading an Image
1. Click the "load" button in the command panel
2. Use the file chooser dialog to select an image file
3. Supported formats: JPEG, JPG, PNG, PPM

### Saving an Image
1. Click the "save" button
2. Choose a destination location and filename
3. Click "save" to confirm
- **Note**: You must save your current image before loading a new one

## Basic Operations

### Reset
- Click "reset" to clear the current image from memory
- This allows you to load a new image without saving

### Image Preview
- The main window displays the current image
- A histogram showing color distribution appears below the image
- For many operations, you'll see a preview before confirming changes

## Image Components
- **Note**: Ensure an image is loaded before performing any of the following operations.
### Color Components
Extract individual color channels:
- Red Component: Click "red-component"
- Green Component: Click "green-component"
- Blue Component: Click "blue-component"
- Luma Component: Click "luma-component"

## Image Transformations

### Flipping
- Vertical Flip: Click "vertical-flip" to mirror the image top-to-bottom
- Horizontal Flip: Click "horizontal-flip" to mirror the image left-to-right

### Downscaling
1. Click "Downscale"
2. Use the sliders to set width and height scaling percentages
3. Click "OK" to apply or "Cancel" to abort

## Effects and Filters

### Blur and Sharpen
- Blur: Click "blur" to apply a Gaussian blur effect
- Sharpen: Click "sharpen" to enhance image details
- Both effects show a split preview:
  1. Use the slider to adjust the preview split position
  2. Click "OK" to apply or "Cancel" to revert

### Sepia
1. Click "sepia" to preview the sepia tone effect
2. Use the slider to compare original and sepia versions
3. Click "OK" to apply or "Cancel" to revert

### Compression
1. Click "compress"
2. Use the slider to set compression percentage (0-100)
3. Click "OK" to apply or "Cancel" to abort

## Color Adjustments

### Color Correction
1. Click "Color Correct"
2. Preview shows split view of original vs corrected
3. Use slider to adjust preview
4. Click "OK" to apply or "Cancel" to revert

### Levels Adjustment
1. Click "levels-adjust"
2. Set three points using spinners:
  - Black Point (0-255)
  - Mid Point (between black and white points)
  - White Point (0-255)
3. Preview shows split view of original vs corrected
4. Use slider to adjust preview
5. Click "OK" to apply or "Cancel" to revert

## Advanced Features

### Split View Preview
Many operations offer a split view preview:
1. The slider controls the split position
2. Left side shows original image
3. Right side shows the effect
4. Move slider to compare results
5. Click "OK" to apply or "Cancel" to revert

### Histogram
- Shows distribution of color values
- Updates automatically with each operation
- Helps visualize image characteristics and changes

## Keyboard Shortcuts
- No keyboard shortcuts are currently implemented