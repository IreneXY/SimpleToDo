# Pre-work - *SimpleToDo*

**SimpleToDo** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Irene Yu**

Time spent: **6** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items into SQLite instead of a text file
* [x] Improve style of the todo items in the list using a custom adapter
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a DialogFragment instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Sort the list by create time, priority or done time.
* [x] Filter the list by all tasks, todo tasks and done tasks.
* [x] Splash screen.
* [x] User friendly blank list UI.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/IreneXY/SimpleToDo/blob/master/android/screenshots/video/simpletodo1.0.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [EZGIF.COM](https://ezgif.com/video-to-gif).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** 
I like coding on the Android platform becaue it supports versatile possibility to explore and implement creative UI and UX. Additionally, when I meet problems, I can check the open-sourced code to learn and resolve problems. It makes me learn technology on the OS level.

Android uses the structure of an XML file to approach layouts. It is very straightforward and follows the hierarchical approach of the view tree. 

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** 
I use the `RecyclerView.Adapter` instead of the `ArrayAdapter` to customize the style of todo items. We can image the Adapter is a link between a data set and an adapter view. And the Adapter is responsible for getting data from the data set and for generating `View` objects based on that data. The generated View objects are then used to populate any adapter view that is bound to the adapter. 

The `convertView` is the second parameter in the `getView` method of the `ArrayAdapter`. When a `ListView` uses an Adapter to fill its rows with Views, the adapter populates each list item with a `View` object by calling `getView` on each row. The Adapter uses the `convertView` as a way of recycling old View objects that are no longer being used. In this way, the `ListView` can send the Adapter old, "recycled" view objects that are no longer being displayed instead of instantiating an entirely new object each time the Adapter wants to display a new list item. This is the purpose of the `convertView` parameter. (https://stackoverflow.com/questions/10560624/what-is-the-purpose-of-convertview-in-listview-adapter)

## License

    Copyright 2017 Irene Yu(https://github.com/IreneXY)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
