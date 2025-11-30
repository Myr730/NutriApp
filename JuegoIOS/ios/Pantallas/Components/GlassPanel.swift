//
//  GlassPanel.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//


import SwiftUI

struct GlassPanel<Content: View>: View {
    let content: () -> Content
    init(@ViewBuilder content: @escaping () -> Content) { self.content = content }
    var body: some View {
        VStack(spacing: Spacing.s16) { content() }
            .padding(Spacing.s20)
            .background(.ultraThinMaterial, in: RoundedRectangle(cornerRadius: AppShape.cornerL, style: .continuous))
            .shadow(color: .black.opacity(0.15), radius: 12, y: 6)
    }
}
