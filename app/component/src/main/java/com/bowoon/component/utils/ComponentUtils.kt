package com.bowoon.component.utils

import com.bowoon.component.data.Component
import com.bowoon.component.data.ComponentType
import com.bowoon.component.data.Components

class ComponentUtils {
    fun createComponent(component: Component): Components =
        when (ComponentType.valueOf(component.type ?: "")) {
            ComponentType.TEXT -> Components.TextComponent(
                component.content?.width,
                component.content?.height,
                component.content?.topMargin,
                component.content?.startMargin,
                component.content?.endMargin,
                component.content?.bottomMargin,
                component.content?.text,
                component.content?.size,
                component.content?.color,
                component.content?.style,
            )
            ComponentType.IMAGE -> Components.ImageComponent(
                component.content?.width,
                component.content?.height,
                component.content?.topMargin,
                component.content?.startMargin,
                component.content?.endMargin,
                component.content?.bottomMargin,
                component.content?.url,
                component.content?.radius,
                component.content?.topLeftRadius,
                component.content?.topRightRadius,
                component.content?.bottomLeftRadius,
                component.content?.bottomRightRadius
            )
            ComponentType.TAB -> Components.TabComponent(
                component.content?.width,
                component.content?.height,
                component.content?.topMargin,
                component.content?.startMargin,
                component.content?.endMargin,
                component.content?.bottomMargin,
                component.content?.mode,
                component.content?.tabs
            )
        }
}