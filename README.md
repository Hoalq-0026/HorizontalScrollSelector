#HorizontalScrollSelector

A HorizontalScrollView with the ability to select an item and center the item's view.
It takes in a `List<View>` and if the `View` implements `Checkable`, the `Selector` will take this into account.
Selecting is possible by either tapping an item or by swiping and centering an item.

##Disclaimer

This is an old project, and the way it works it not nice.
It works and you are free to use it, but it would be better to create something similar using an Adapter, or a RecyclerView with a custom LayoutManager based on the horizontal linear layout manager.

In short: Use at your own risk :-)

#Usage

*For a working example, check the sample module*

1. Add the widget to your view

		<com.kevinpelgrims.horizontalscrollselector.library.Selector
            android:id="@+id/selector"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

2. Add a list of views

		Selector selector = (Selector) findViewById(R.id.selector);
		selector.setData(checkableViewList);

##Add to your project (Gradle)

Build and add resulting .aar to 'aars' folder in the module root.

Add following lines in build.gradle:

	repositories{
		flatDir {
			dirs 'aars'
		}
	}

	dependencies {
		compile 'com.kevinpelgrims.horizontalscrollselector:library:0.1.0@aar'
	}

#License

"THE BEER-WARE LICENSE" (Revision 42):
 
<kevin.pelgrims@gmail.com> wrote this library.
As long as you retain this notice you can do whatever you want with this stuff.
If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 
Kevin Pelgrims
