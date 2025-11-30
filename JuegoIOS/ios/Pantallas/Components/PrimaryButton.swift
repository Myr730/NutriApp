//
//  PrimaryButton.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//


import SwiftUI

struct PrimaryButton: View {
    var title: LocalizedStringKey
    
    let action: () -> Void

    var body: some View {
        Button(action: action){
            Text(title)
                .font(TypeScale.titleM)
                .foregroundStyle(AppColor.onPrimary)
                .frame(maxWidth: .infinity, minHeight: 52)
                .padding(.vertical, Spacing.s12)
                .background(
                    RoundedRectangle(cornerRadius: AppShape.cornerL, style: .continuous)
                        .fill(AppColor.primary)
                )
                .shadow(color: .black.opacity(0.18), radius: 8, y: 4)
                .accessibilityLabel(title)
        }
        
    }
}
