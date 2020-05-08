//
//  Player.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/27/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import UIKit

class Player {
    
    // MARK: Properties
    
    var name: String
    var average: Int
    var values: [Int]
    var lastPasses: [Int]
    
    struct PropertyKey {
        static let name = "name"
        static let average = "average"
        static let values = "values"
        static let lastPasses = "lastPasses"
    }
    
    
    // MARK: Initialization
     
    init?(_ name: String?) {
        // The name must not be empty
        guard name != nil else {
            return nil
        }
        
        self.name = name!
        average = 0
        values = [0, 0, 0, 0]
        lastPasses = []
    }
    
    convenience init?() {
        self.init("")
    }
    
    
    // MARK: Class methods
    
    func getStatsText() -> String {
        return "Average: \(Double(round(getPassesAndAvg().avg*1000)/1000)) (\(getPassesAndAvg().passes) passes)\nZeros: \(values[0]) Ones: \(values[1]) Twos: \(values[2]) Threes: \(values[3])"
    }
    
    func reset() {
        average = 0
        values = [0, 0, 0, 0]
        lastPasses = []
    }
    
    func resetAll() {
        name = ""
        reset()
    }
    
    
    func getPassesAndAvg() -> (passes: Int, avg: Double) {
        var passes = 0
        var sum = 0
        for i in 0..<values.count {
            sum += i * values[i]
            passes += values[i]
        }
        
        if passes == 0 {
            return (0, 0.0)
        }
        
        return (passes, (Double(sum) / Double(passes)))
    }
}
