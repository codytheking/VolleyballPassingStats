//
//  Player.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/27/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import UIKit

class Player {
    
    // Mark: Properties
    
    var name: String
    var average: Int
    var values: [Int]
    
    struct PropertyKey {
        static let name = "name"
        static let average = "average"
        static let values = "values"
    }
    
    
    //MARK: Initialization
     
    init?(_ name: String) {
        // The name must not be empty
        guard !name.isEmpty else {
            return nil
        }
        
        self.name = name
        average = 0
        values = [0, 0, 0, 0]
    }
    
    func getSum() -> Int {
        return values.reduce(0, +)
    }
    
    func getStatsText() -> String {
        return "Average \(getSum()):\nZeros: \(values[0]) Ones: \(values[1]) Twos: \(values[2]) Threes: \(values[3])"
    }
    
    func reset() {
        average = 0
        values = [0, 0, 0, 0]
    }
}
