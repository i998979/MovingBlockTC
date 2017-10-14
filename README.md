# MovingBlockTC

**This project is discontinued since TrainCarts implemented its own "Moving Block" system**

This is a minecraft plugin that depends on TrainCarts and make trains move and stop itself without using a bunch of waiter signs.  
It count the distance between train and train and stop itself if it is too close.  
Also You can change the train speed instantly with a command.  
Remember this plugin will do the check all the TrainCarts every 10 tick to apply changes on them if they have "MovingBlock" system support.  

## **Dependencies**
- TrainCarts
- BKCommonLib


## **Commands**
Main commands:
- "": necessary []: optional  
- /mbtc: Shows the help page for the plugin
- /mbtc getProperties: Get the properties of the riding train
- /mbtc setCurrentSpeed <distance > <target speed>: Set the current speed of the train
- /mbtc getCurrentSpeed: Get the current running speed of the riding train
- /mbtc getMaxSpeed: Get the max speed of the riding train
- /mbtc SetMaxSpeed: Set the max speed of the riding train
- /mbtc getLength: Get the length of the riding train
- /mbtc hasMovingBlock: Tell you if this train has MovingBlock support or not
- /mbtc checkdistance "distance to check": Check if there is any train ahead within certain blocks, if yes, the train stops


## **Signs**
**MovingBlock supporting sign**
- Adding a tag to the train and tell the system this train can run "MovingBlock" system  
Line 1: [train]  
Line 2: property  
Line 3: addtag  
Line 4: MovingBlock  

**MovingBlock supporting removal sign**
- Removing a tag from the train and tell the system this train no longer "MovingBlock" system  
Line 1: [train]  
Line 2: property  
Line 3: remtag  
Line 4: MovingBlock  


## **Future**
- Currently nothing since TrainCarts author said that what this plugin does should be inside TrainCarts itself

## **Terms of Use**
- You are allowed to implement your own plugin base on this plugin if you credit me and the TrainCarts author.
- You are not allowed to redistribute any part of the code and claim that is your work.

## **Videos**
https://www.youtube.com/watch?v=fiLBl82HhTQ
