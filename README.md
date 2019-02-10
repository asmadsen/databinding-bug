# Two-Way Data Binding Bug

This is a reproduction of the Two-Way Data binding bug.
This project was created using the "New Project"-wizard in Android Studio.
Most changes is described throughout the code.

## Steps to reproduce

1. Run app
2. Press "Go to next Fragment", then press the back-button - *This should work as expected*
3. Check the currently unchecked RadioButton, then press "Go to next Fragment" again
4. Press the back-button, and then the RadioButtons will run into an endless loop checking each other.

## Bug description
The issue happens when persisting the state of the RadioButtons in the
ViewModel and that state is connected to the RadioGroup using
[Two-way Data Binding](https://developer.android.com/topic/libraries/data-binding/two-way).

I think this issue relates to the built in functionality of the
[RadioGroup widget](https://developer.android.com/reference/android/widget/RadioGroup).
It should not uncheck the currently checked RadioButton unless the
change is an unchecked RadioButton being checked.

Maybe it even should have the ability to disable the built in state 
preservation, either automatically when using Databinding or explicitly.

```java
private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // prevents from infinite recursion
        if (mProtectFromCheckedChange) {
            return;
        }
        mProtectFromCheckedChange = true;
        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false); // It will uncheck the currently checked button regardless of the state of isChecked
        }
        mProtectFromCheckedChange = false;
        int id = buttonView.getId();
        setCheckedId(id);
    }
} 
```
[Source](https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/widget/RadioGroup.java#363)

## Notes
I am using [Navigation Architectural Component](https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing)
to navigate between the two fragments. This shouldn't have any effect on the issue.