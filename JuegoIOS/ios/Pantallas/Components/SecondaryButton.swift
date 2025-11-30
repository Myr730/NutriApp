//
//  SecondaryButton.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//

import SwiftUI

struct SecondaryButton: View {
    var title: LocalizedStringKey
    let action: () -> Void
    var body: some View {
        Text(title)
            .font(TypeScale.titleM)
            .foregroundStyle(AppColor.primary)
            .frame(maxWidth: .infinity, minHeight: 52)
            .padding(.vertical, Spacing.s12)
            .background(
                RoundedRectangle(cornerRadius: AppShape.cornerL, style: .continuous)
                    .fill(AppColor.surface.opacity(0.9))
            )
            .overlay(
                RoundedRectangle(cornerRadius: AppShape.cornerL, style: .continuous)
                    .stroke(AppColor.primary, lineWidth: 2)
            )
            .shadow(color: .black.opacity(0.08), radius: 6, y: 3)
            .accessibilityLabel(title)
    }
}
