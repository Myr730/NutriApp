//
//  CardContainer.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//


import SwiftUI

// Solución para Cannot find 'CardContainer'
struct CardContainer<Content: View>: View {
    @ViewBuilder var content: Content
    
    var body: some View {
        VStack(alignment: .leading, spacing: Spacing.s8) {
            content
        }
        .padding(Spacing.s16)
        .background(
            RoundedRectangle(cornerRadius: AppShape.cornerL)
                .fill(AppColor.surface)
                .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 2)
        )
    }
}
