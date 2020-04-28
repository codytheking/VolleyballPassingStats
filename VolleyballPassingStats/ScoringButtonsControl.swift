//
//  ScoringButtonsControl.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/27/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import UIKit

@IBDesignable class ScoringButtonsControl: UIStackView {
    
    //MARK: Properties
    private var scoringButtons = [UIButton]()
    var scores = [0, 0, 0, 0]
    
    @IBInspectable var buttonSize: CGSize = CGSize(width: 60.0, height: 40.0) {
        didSet {
            setupButtons()
        }
    }
    @IBInspectable var buttonCount: Int = 4 {
        didSet {
            setupButtons()
        }
    }
    
    
    // MARK: Initialization
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupButtons()
    }
    
    required init(coder: NSCoder) {
        super.init(coder: coder)
        setupButtons()
    }
    
    
    // MARK: Button Action
    
    @objc func scoringButtonTapped(button: UIButton) {
        guard let index = scoringButtons.firstIndex(of: button) else {
            fatalError("The button, \(button), is not in the scoringButtons array: \(scoringButtons)")
        }
        
        print("Button pressed 👍")
        
        // TODO: Do stuff with scoring
        
    }
    
    
    // MARK: Private Methods
    
    private func setupButtons() {
        // clear any existing buttons
        for button in scoringButtons {
            removeArrangedSubview(button)
            button.removeFromSuperview()
        }
        scoringButtons.removeAll()

        
        for _ in 0..<buttonCount {
            // Create the button
            let button = UIButton()
            button.backgroundColor = UIColor.red
            
            
            // Add constraints
            // The first line of code disables the button’s automatically generated constraints.
            /*
             The lines starting with button.heightAnchor and button.widthAnchor create the constraints
             that define the button’s height and width. Each line performs the following steps:
             
             The button’s heightAnchor and widthAnchor properties give access to layout anchors.
             You use layout anchors to create constraints—in this case, constraints that define the
             view’s height and width, respectively.
             
             The anchor’s constraint(equalToConstant:) method returns a constraint that defines a
             constant height or width for the view.
             
             The constraint’s isActive property activates or deactivates the constraint. When you set
             this property to true, the system adds the constraint to the correct view, and activates it.
             
             Together, these lines define the button as a fixed-size object in your layout (44 point x 44 point).
             */
            button.translatesAutoresizingMaskIntoConstraints = false
            button.heightAnchor.constraint(equalToConstant: buttonSize.height).isActive = true
            button.widthAnchor.constraint(equalToConstant: buttonSize.width).isActive = true
            
            // Setup the button action
            button.addTarget(self, action: #selector(ScoringButtonsControl.scoringButtonTapped(button:)), for: .touchUpInside)
            
            // Add the button to the stack
            addArrangedSubview(button)
            
            // Add the new button to the rating button array
            scoringButtons.append(button)
        }
    }
}
