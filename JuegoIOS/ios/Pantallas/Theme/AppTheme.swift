//
//  AppTheme.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//

import SwiftUI

struct AppTheme<Content: View>: View {
    let content: () -> Content
    init(@ViewBuilder _ content: @escaping () -> Content) { self.content = content }

    var body: some View {
        content()
            .tint(AppColor.primary)
            .environment(\.font, TypeScale.bodyM)
            .background(AppColor.background.ignoresSafeArea())
    }
}
