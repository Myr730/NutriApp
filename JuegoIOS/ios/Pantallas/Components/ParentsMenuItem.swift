//
//  ParentsMenultem.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//

import SwiftUI

struct ParentsMenuItem: View {
    var title: LocalizedStringKey
    var systemImage: String
    var bg: Color

    var body: some View {
        HStack(spacing: Spacing.s16) {
            ZStack {
                Circle()
                    .fill(.white.opacity(0.9))
                    .frame(width: 44, height: 44)
                Image(systemName: systemImage)
                    .font(.system(size: 22, weight: .semibold))
                    .foregroundStyle(bg)
            }

            Text(title)
                .font(TypeScale.titleM)
                .lineLimit(2)
                .foregroundStyle(.white)

            Spacer(minLength: 0)
        }
        .padding(.vertical, Spacing.s16)
        .padding(.horizontal, Spacing.s16)
        .background(
            RoundedRectangle(cornerRadius: 18, style: .continuous)
                .fill(bg)
        )
        .shadow(color: .black.opacity(0.1), radius: 6, y: 3)
        .accessibilityLabel(title)
    }
}

#Preview {
    VStack(spacing: 16) {
        ParentsMenuItem(title: "parents_menu_progress",        systemImage: "chart.bar.fill", bg: Color(hex:"#F7B733"))
        ParentsMenuItem(title: "parents_menu_weight_height",   systemImage: "scalemass.fill", bg: Color(hex:"#F39C12"))
        ParentsMenuItem(title: "parents_menu_tips",            systemImage: "book.fill",      bg: Color(hex:"#2BBBAD"))
        ParentsMenuItem(title: "parents_menu_playtime",        systemImage: "alarm.fill",     bg: Color(hex:"#F06292"))
    }
    .padding()
    .background(Color(hex:"#F7F3EA"))
}
