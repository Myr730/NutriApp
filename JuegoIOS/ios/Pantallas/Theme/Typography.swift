//
//  Typography.swift
//  Aventuras con Nutri
//
//  Created by Victoria García on 21/09/25.
//

import SwiftUI

enum TypeScale {
    static let titleXL  = Font.system(.largeTitle, design: .rounded).weight(.heavy)
    static let titleL   = Font.system(.title,      design: .rounded).weight(.bold)
    static let titleM   = Font.system(.title3,     design: .rounded).weight(.semibold)
    static let bodyM    = Font.system(.body,       design: .rounded)
    static let captionM = Font.system(.caption,    design: .rounded)
}
