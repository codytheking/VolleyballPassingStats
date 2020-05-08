//
//  VolleyballPassingStatsTests.swift
//  VolleyballPassingStatsTests
//
//  Created by Cody King on 4/26/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import XCTest
@testable import VolleyballPassingStats

class VolleyballPassingStatsTests: XCTestCase {

    /*override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() throws {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
    }

    func testPerformanceExample() throws {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }*/
    
    
    // Player tests
    
    // Confirm that the Player initializer returns a Player object when passed valid parameters.
    func testPlayerInitializationSucceeds() {
        let createPlayer = Player.init("King")
        XCTAssertNotNil(createPlayer)
        
        let emptyName = Player.init()
        XCTAssertNotNil(emptyName)
    }
}
